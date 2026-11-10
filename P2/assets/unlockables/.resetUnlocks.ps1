$scriptsToRun = @(
    "$PSScriptRoot\Colors\.colorGenerator.ps1",
    "$PSScriptRoot\Skins\.skinGenerator.ps1"
)

foreach ($script in $scriptsToRun) {
    # Resolve the absolute path so PowerShell doesn't get lost
    $targetScript = (Resolve-Path $script -ErrorAction SilentlyContinue).Path

    if ($targetScript) {
        Write-Host "Executing: $targetScript" -ForegroundColor Cyan
        
        # Execute script, retain original colors, and indent each line with a tab (`t)
        & $targetScript *>&1 | ForEach-Object {
            if ($_ -is [System.Management.Automation.InformationRecord] -and $_.MessageData -is [System.Management.Automation.HostInformationMessage]) {
                $msg = $_.MessageData
                $params = @{ Object = "`t$($msg.Message)" }
                
                if ($null -ne $msg.ForegroundColor -and [int]$msg.ForegroundColor -ge 0) {
                    $params['ForegroundColor'] = $msg.ForegroundColor
                }
                if ($null -ne $msg.BackgroundColor -and [int]$msg.BackgroundColor -ge 0) {
                    $params['BackgroundColor'] = $msg.BackgroundColor
                }
                
                Write-Host @params
            } else {
                # Fallback for standard output or uncolored strings
                Write-Host "`t$_"
            }
        }
    } else {
        Write-Host "File not found: $script" -ForegroundColor Red
    }
}