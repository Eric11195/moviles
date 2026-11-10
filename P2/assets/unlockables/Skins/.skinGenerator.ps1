# Create a UTF-8 encoding object without a Byte Order Mark (BOM)
$utf8NoBom = [System.Text.UTF8Encoding]::new($false)

# 1. Element types array
$elementTypes = @(        
        "PLAYER",
        "MISSILE",
        "LASER",
        "WARNINGS",
        "ZAPPER",
        "COINS"
) 

# 2. Define prices
$shopPrice    = 0
$defaultPrice = -1

$skinsPerType = @(
    #Player Skins
    @($null, "img/test_image.jpg"),
    #Missile Skins
    @($null),
    #Laser Skins
    @($null),
    #Warning Skins
    @($null),
    #Zapper Skins
    @($null),
    #Coins Skins
    @($null)
)

# 3. Generate shop skin files in script directory without BOM
for ($i = 0; $i -lt $elementTypes.Count; $i++) {
    $type = $elementTypes[$i]
    
    for ($j = 0; $j -lt $skinsPerType[$i].Count; $j++) {   
        $skinFile = $skinsPerType[$i][$j]
        $fileName = "{0}_skin{1}.json" -f $type, $j
        $filePath = Join-Path -Path $PSScriptRoot -ChildPath $fileName

        $priceOfThis = $shopPrice
        if ($j -eq 0) {
            $priceOfThis = $defaultPrice
        }

        if($skinFile -eq $null){
            $jsonObj = [ordered]@{
                type     = "skin"
                imageFor = $type
                # Strips "assets/" or "assets\" from the beginning of the path
                path     = $skinFile 
                price    = $priceOfThis
            }
        }else{
            $jsonObj = [ordered]@{
                type     = "skin"
                imageFor = $type
                # Strips "assets/" or "assets\" from the beginning of the path
                path     = $skinFile -replace '^assets[\\/]', '' 
                price    = $priceOfThis
            }
        }
        
        $jsonContent = $jsonObj | ConvertTo-Json
        [System.IO.File]::WriteAllText($filePath, $jsonContent, $utf8NoBom)
        Write-Host "Created: $fileName" -ForegroundColor Cyan
    }
}