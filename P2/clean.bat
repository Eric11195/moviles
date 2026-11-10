@echo off
:: Set working directory to where this batch file is stored
cd /d "%~dp0"

echo Cleaning all ignored files in current repository and subdirectories...
git clean -fdX

echo Cleaning completed.
pause