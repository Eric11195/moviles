@echo off
powershell.exe -ExecutionPolicy Bypass -File "%~dp0.resetGameToDefault.ps1"
powershell.exe -ExecutionPolicy Bypass -File "%~dp0.resetLevelToDefault.ps1"
pause