# Create a UTF-8 encoding object without a Byte Order Mark (BOM)
$utf8NoBom = [System.Text.UTF8Encoding]::new($false)

# 1. Element types array
$elementTypes = @(        
    "BUTTON_FONT",
    "COIN_N_DISTANCE_COUNT",
    "TITLE_FONT"
) 

# 2. Define prices
$shopPrice    = 0
$defaultPrice = -1

# Fonts dictionary
$fonts = [ordered]@{
    "Arial" = $null
    "FascinateInline_Regular" = "FascinateInline-Regular.ttf"
}
$defaultFontSize = 40

# 4. Generate shop font files in script directory
foreach ($type in $elementTypes) {
    $isFirst = $true
    
    foreach ($f in $fonts.GetEnumerator()) {
        # Only the first font is unlocked (price 0), others are default/locked (-1)
        $priceToWrite = if ($isFirst) { $defaultPrice } else { $shopPrice }
        
        $fileName = "{0}_{1}.json" -f $type, $f.name
        $filePath = Join-Path -Path $PSScriptRoot -ChildPath $fileName
        
        $jsonObj = [ordered]@{
            type     = "font"
            fontFor = $type
            fontName = $f.value
            fontSize = $defaultFontSize
            price    = $priceToWrite
        }
        
        $jsonContent = $jsonObj | ConvertTo-Json
        [System.IO.File]::WriteAllText($filePath, $jsonContent, $utf8NoBom)
        Write-Host "Created: $fileName (Price: $priceToWrite)" -ForegroundColor Green
        
        $isFirst = $false
    }
}