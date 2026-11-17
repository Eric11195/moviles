# Create a UTF-8 encoding object without a Byte Order Mark (BOM)
$utf8NoBom = [System.Text.UTF8Encoding]::new($false)

# 1. Define price and dictionary mapping: "Name/Description" = CustomKey
$shopPrice = 0
$startKey = 3112

$names = [ordered]@{
    "Forest"  = $startKey + 1
    "Desert"  = $startKey + 5
    "Volcano" = $startKey + 12
}

# 2. Generate a JSON file for each entry in the dictionary
foreach ($entry in $names.GetEnumerator()) {
    $description = $entry.Key
    $customKey   = $entry.Value

    $fileName = "{0}.json" -f $description
    $filePath = Join-Path -Path $PSScriptRoot -ChildPath $fileName

    $jsonObj = [ordered]@{
        type        = "levelKey"
        price       = $shopPrice
        description = $description
        key         = $customKey
    }

    $jsonContent = $jsonObj | ConvertTo-Json
    [System.IO.File]::WriteAllText($filePath, $jsonContent, $utf8NoBom)
    Write-Host "Created: $fileName (Key: $customKey, Price: $shopPrice)" -ForegroundColor Green
}