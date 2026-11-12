
# ==========================================
# ELIMINAR CARPETA LEVELS DEL EMULADOR
# ==========================================

$paquete = "com.example.android"
$ruta = "files/levels"

Write-Host ""
Write-Host "==================================" -ForegroundColor Cyan
Write-Host "   LIMPIEZA DE NIVELES ANDROID" -ForegroundColor Cyan
Write-Host "==================================" -ForegroundColor Cyan

# Comprobar ADB
$adb = "$env:LOCALAPPDATA\Android\Sdk\platform-tools\adb.exe"

if (-not (Test-Path $adb)) {
    Write-Host "ERROR: No se encuentra adb.exe" -ForegroundColor Red
    exit 1
}

# Comprobar dispositivo
& $adb get-state 2>$null

if ($LASTEXITCODE -ne 0) {
    Write-Host "ERROR: No hay emulador conectado." -ForegroundColor Red
    exit 1
}

# Comprobar paquete
& $adb shell run-as $paquete ls files 2>$null

if ($LASTEXITCODE -ne 0) {
    Write-Host "ERROR: No se puede acceder al paquete." -ForegroundColor Red
    Write-Host "Comprueba que la app esta instalada y es debuggable."
    exit 1
}


# Eliminar carpeta
& $adb shell run-as $paquete rm -rf $ruta

if ($LASTEXITCODE -eq 0) {
    Write-Host ""
    Write-Host "CARPETA LEVELS ELIMINADA CORRECTAMENTE." -ForegroundColor Green
} else {
    Write-Host "ERROR AL ELIMINAR LA CARPETA." -ForegroundColor Red
    exit 1
}

Write-Host ""
Read-Host "Pulsa ENTER para salir"
