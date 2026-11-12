# Create a UTF-8 encoding object without a Byte Order Mark (BOM)
$utf8NoBom = [System.Text.UTF8Encoding]::new($false)

# 1. Element types array
$elementTypes = @(        
    "shieldCount",
    "extraStartMeters",
    "extraStartSpeed"
) 

# 2. Define prices
$shopPrice    = 0

$valuesPerType = @(
    #shieldCount
    @(1, 1, 1),
    #extraStartMeters
    @(20,20,20,20),
    #extraStartSpeed
    @(10, 10, 10, 10)
)

# 3. Generate shop skin files in script directory without BOM
for ($i = 0; $i -lt $elementTypes.Count; $i++) {
    $type = $elementTypes[$i]
    
    for ($j = 0; $j -lt $valuesPerType[$i].Count; $j++) {   
        $myValue = $valuesPerType[$i][$j]
        $fileName = "{0}_skin{1}.json" -f $type, $j
        $filePath = Join-Path -Path $PSScriptRoot -ChildPath $fileName

        $jsonObj = [ordered]@{
            type     = "powerUp"
            var = $type
            value     = $myValue 
            price    = $shopPrice
        }
        
        $jsonContent = $jsonObj | ConvertTo-Json
        [System.IO.File]::WriteAllText($filePath, $jsonContent, $utf8NoBom)
        Write-Host "Created: $fileName" -ForegroundColor Cyan
    }
}