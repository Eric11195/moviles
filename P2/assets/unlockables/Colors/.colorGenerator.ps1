# 1. Define your Enum types here
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

# 2. Define the fixed price
$price = 5

# 3. Define colors cast as [long] so they never output as negative in the JSON
$colors = [ordered]@{
    "Crimson"    = [long]0xFFE74C3C
    "Tangerine"  = [long]0xFFE67E22
    "Sunflower"  = [long]0xFFF1C40F
    "Emerald"    = [long]0xFF2ECC71
    "Mint"       = [long]0xFF1ABC9C
    "Ocean"      = [long]0xFF3498DB
    "Amethyst"   = [long]0xFF9B59B6
    "Navy"       = [long]0xFF2C3E50
    "Silver"     = [long]0xFFBDC3C7
    "Charcoal"   = [long]0xFF34495E
}

# 4. Generate the files
foreach ($type in $elementTypes) {
    foreach ($color in $colors.GetEnumerator()) {
        $fileName = "{0}_{1}.json" -f $type, $color.Name
        
        $jsonObj = [ordered]@{
            type = "color"
            colorFor = $type
            color    = $color.Value
            price    = $price
        }
        
        # Changed encoding to 'ascii' to prevent the invisible BOM character from breaking Java
        $jsonObj | ConvertTo-Json | Out-File -FilePath $fileName -Encoding ascii
        Write-Host "Created: $fileName" -ForegroundColor Green
    }
}