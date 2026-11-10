# Get all files (including hidden ones) in the current directory
Get-ChildItem -Path . -File -Force | Where-Object {
    $_.Name.StartsWith(".") -and 
    ($_.Name.EndsWith(".colors.json") -or $_.Name.EndsWith(".skins.json") -or $_.Name.EndsWith(".fonts.json"))
} | ForEach-Object {
    # Remove the first character (the initial dot)
    $newName = $_.Name.Substring(1)
    
    # Copy the file to the new name (-Force overwrites if the target already exists)
    Copy-Item -Path $_.FullName -Destination (Join-Path $_.DirectoryName $newName) -Force
    
    Write-Host "Copied: $($_.Name) -> $newName" -ForegroundColor Green
}