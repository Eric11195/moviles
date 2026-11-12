# Create a UTF-8 encoding object without a Byte Order Mark (BOM)
$utf8NoBom = [System.Text.UTF8Encoding]::new($false)

# 1. Element types array
$elementTypes = @(        
        "PLAYER",
        "MISSILE",
        "LASER",
        "WARNINGS",
        "ZAPPER",
        "COINS",
        "BUTTON"
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
    @($null),
    #Buttons
    @(
        $null,
        "img/UI_Flat_Banner02a.png",
        "img/UI_Flat_Banner03a.png",
        "img/UI_Flat_Button01a_3.png",
        "img/UI_Flat_Button02a_4.png",
        "img/UI_Flat_FrameSlot01a.png",
        "img/UI_Flat_FrameSlot01b.png",
        "img/UI_Flat_FrameSlot01c.png",
        "img/UI_Flat_FrameSlot02a.png",
        "img/UI_Flat_FrameSlot02b.png",
        "img/UI_Flat_FrameSlot02c.png",
        "img/UI_Flat_InputField02a.png",
        "img/UI_Flat_Select01a_1.png",
        "img/UI_Flat_Select02a_1.png"
    )
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