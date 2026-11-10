# 1. Element types array (removed invalid [ordered] modifier)
$elementTypes = @(        
        "PLAYER",
        "MISSILE",
        "LASER",
        "WARNINGS",
        "ZAPPER",
        "COINS"
) 

# 2. Define prices
$shopPrice    = 5
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
    #Coins SKins
    @($null)
)

# 3. Generate shop skin files
for ($i = 0; $i -lt $elementTypes.Count; $i++) {
    $type     = $elementTypes[$i]
    for($j = 0; $j -lt $skinsPerType[$i].Count; $j++){   
        $skinFile = $skinsPerType[$i][$j]
        $fileName = "{0}_skin{1}.json" -f $type, $j

        $priceOfThis = $shopPrice
        if($j -eq 0){
            $priceOfThis = $defaultPrice
        }

        $jsonObj = [ordered]@{
            type     = "skin"
            imageFor = $type
            path    = $skinFile
            price    = $priceOfThis
        }
        $jsonObj | ConvertTo-Json | Out-File -FilePath $fileName -Encoding ascii
        Write-Host "Created default: $fileName" -ForegroundColor Cyan
    }
}