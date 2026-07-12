@echo off
setlocal enabledelayedexpansion
title MyCoolClient - Auto Build
color 0B

echo ============================================
echo   MyCoolClient - Automatic Build (Windows)
echo ============================================
echo.

REM --- Шаг 1: проверка Java ---
echo [1/4] Checking Java...
java -version >nul 2>&1
if errorlevel 1 (
    echo.
    echo Java NOT found on this PC.
    echo Opening download page for JDK 17 - install it first, then run this script again.
    start https://adoptium.net/temurin/releases/?version=17
    pause
    exit /b 1
)
echo Java found. OK.
echo.

REM --- Шаг 2: скачиваем Gradle, если его ещё нет ---
set GRADLE_VERSION=7.4
set GRADLE_DIR=%~dp0gradle-%GRADLE_VERSION%
if not exist "%GRADLE_DIR%" (
    echo [2/4] Downloading Gradle %GRADLE_VERSION% ^(~120 MB, needs internet^)...
    curl -L -o "%~dp0gradle.zip" https://services.gradle.org/distributions/gradle-%GRADLE_VERSION%-bin.zip
    if errorlevel 1 (
        echo Failed to download Gradle. Check your internet connection.
        pause
        exit /b 1
    )
    echo Extracting...
    powershell -Command "Expand-Archive -Path '%~dp0gradle.zip' -DestinationPath '%~dp0' -Force"
    del "%~dp0gradle.zip"
) else (
    echo [2/4] Gradle already downloaded, skipping.
)
echo.

REM --- Шаг 3: генерируем gradlew (wrapper), если его ещё нет ---
if not exist "%~dp0gradlew.bat" (
    echo [3/4] Generating Gradle wrapper for the project...
    call "%GRADLE_DIR%\bin\gradle.bat" -p "%~dp0" wrapper --gradle-version %GRADLE_VERSION%
) else (
    echo [3/4] Gradle wrapper already exists, skipping.
)
echo.

REM --- Шаг 4: сборка мода ---
echo [4/4] Building the mod (first build downloads Minecraft 1.16.5 + Fabric libs, can take 5-10 min)...
call "%~dp0gradlew.bat" build

if errorlevel 1 (
    echo.
    echo ============================================
    echo   BUILD FAILED. Scroll up to see the error.
    echo ============================================
    pause
    exit /b 1
)

echo.
echo ============================================
echo   BUILD SUCCESSFUL!
echo   Your mod jar is in: build\libs\mycoolclient-1.0.0.jar
echo ============================================
echo.
echo Opening the output folder...
start "" "%~dp0build\libs"

echo.
echo NEXT STEPS TO PLAY:
echo  1. Install Fabric Loader 1.16.5 via https://fabricmc.net/use/installer/
echo  2. Download Fabric API 1.16.5 from https://modrinth.com/mod/fabric-api
echo  3. Put both fabric-api jar AND mycoolclient-1.0.0.jar into %%appdata%%\.minecraft\mods
echo  4. Launch Minecraft with the "fabric-loader-1.16.5" profile
echo  5. In-game press RIGHT SHIFT to open the GUI
echo.
pause
