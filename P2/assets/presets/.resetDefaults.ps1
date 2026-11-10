# 1. Target the script's actual directory instead of the current working directory
$targetFolder = $PSScriptRoot

Get-ChildItem -Path $targetFolder -File -Force | Where-Object {
    $_.Name.StartsWith(".") -and 
    ($_.Name -match "\.(colors|skins|fonts)\.json$") # Cleaner matching for multiple extensions
} | ForEach-Object {
    
    # Remove the first character (the initial dot)
    $newName = $_.Name.Substring(1)
    
    # Define absolute destination path
    $destination = Join-Path $_.DirectoryName $newName
    
    # Copy the file to the new name (-Force overwrites if the target already exists)
    Copy-Item -Path $_.FullName -Destination $destination -Force
    
    # Verification output
    if (Test-Path $destination) {
        Write-Host "Copied: $($_.Name) -> $newName" -ForegroundColor Green
    } else {
        Write-Host "Failed to copy: $($_.Name)" -ForegroundColor Red
    }
}