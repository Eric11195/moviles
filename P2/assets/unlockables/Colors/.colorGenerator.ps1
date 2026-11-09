# 1. Element types array (removed invalid [ordered] modifier)
$elementTypes = @(        
    "PLAYER_COLOR",
    "LASER_COLOR",
    "ZAPPER_COLOR",
    "MISSILE_COLOR",
    "COIN_COLOR",
    "TEXT_COLOR",
    "BACKGROUND_COLOR",
    "BUTTONS_COLOR"
) 

# 2. Define prices
$shopPrice    = 5
$defaultPrice = 0

# Default color values formatted as a long array matching the order of $elementTypes
$defaultColors = @(
    [long]4288059030, # PLAYER_COLOR
    [long]4294967040, # LASER_COLOR
    [long]4294967040, # ZAPPER_COLOR
    [long]4278190335, # MISSILE_COLOR
    [long]4294953035, # COIN_COLOR
    [long]4278190080, # TEXT_COLOR
    [long]4294967295, # BACKGROUND_COLOR
    [long]4294901760  # BUTTONS_COLOR
)

# 3. Define palette colors cast as [long]
$colors = [ordered]@{
    "Crimson"   = [long]0xFFE74C3C
    "Tangerine" = [long]0xFFE67E22
    "Sunflower" = [long]0xFFF1C40F
    "Emerald"   = [long]0xFF2ECC71
    "Mint"      = [long]0xFF1ABC9C
    "Ocean"     = [long]0xFF3498DB
    "Amethyst"  = [long]0xFF9B59B6
    "Navy"      = [long]0xFF2C3E50
    "Silver"    = [long]0xFFBDC3C7
    "Charcoal"  = [long]0xFF34495E
}

# 4. Generate shop color files
foreach ($type in $elementTypes) {
    foreach ($color in $colors.GetEnumerator()) {
        $fileName = "{0}_{1}.json" -f $type, $color.Name
        
        $jsonObj = [ordered]@{
            type     = "color"
            colorFor = $type
            color    = $color.Value
            price    = $shopPrice
        }
        
        $jsonObj | ConvertTo-Json | Out-File -FilePath $fileName -Encoding ascii
        Write-Host "Created: $fileName" -ForegroundColor Green
    }
}

# 5. Generate default color files
for ($i = 0; $i -lt $elementTypes.Count; $i++) {
    $type     = $elementTypes[$i]
    $fileName = "{0}_default.json" -f $type
    
    $jsonObj = [ordered]@{
        type     = "color"
        colorFor = $type
        color    = $defaultColors[$i]
        price    = $defaultPrice
    }
    
    $jsonObj | ConvertTo-Json | Out-File -FilePath $fileName -Encoding ascii
    Write-Host "Created default: $fileName" -ForegroundColor Cyan
}