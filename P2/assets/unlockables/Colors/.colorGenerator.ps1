# Create a UTF-8 encoding object without a Byte Order Mark (BOM)
$utf8NoBom = [System.Text.UTF8Encoding]::new($false)

# 1. Element types array
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
$shopPrice    = 0
$defaultPrice = -1

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

# 4. Generate shop color files in script directory
foreach ($type in $elementTypes) {
    foreach ($color in $colors.GetEnumerator()) {
        $fileName = "{0}_{1}.json" -f $type, $color.Name
        $filePath = Join-Path -Path $PSScriptRoot -ChildPath $fileName
        
        $jsonObj = [ordered]@{
            type     = "color"
            colorFor = $type
            color    = $color.Value
            price    = $shopPrice
        }
        
        $jsonContent = $jsonObj | ConvertTo-Json
        [System.IO.File]::WriteAllText($filePath, $jsonContent, $utf8NoBom)
        Write-Host "Created: $fileName" -ForegroundColor Green
    }
}

# 5. Generate default color files in script directory
for ($i = 0; $i -lt $elementTypes.Count; $i++) {
    $type     = $elementTypes[$i]
    $fileName = "{0}_default.json" -f $type
    $filePath = Join-Path -Path $PSScriptRoot -ChildPath $fileName
    
    $jsonObj = [ordered]@{
        type     = "color"
        colorFor = $type
        color    = $defaultColors[$i]
        price    = $defaultPrice
    }
    
    $jsonContent = $jsonObj | ConvertTo-Json
    [System.IO.File]::WriteAllText($filePath, $jsonContent, $utf8NoBom)
    Write-Host "Created default: $fileName" -ForegroundColor Cyan
}