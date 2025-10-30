@echo off
REM ============================================================
REM start-server.bat - Inicia o servidor AgendaSaaS (modo Dev)
REM ============================================================
SETLOCAL ENABLEDELAYEDEXPANSION

title AgendaSaaS Server

echo ========================================
echo         AGENDA SAAS - SERVIDOR
echo ========================================

REM Determina o diretório do script (pasta bin do Tomcat)
SET "SCRIPT_DIR=%~dp0"
IF "%SCRIPT_DIR:~-1%"=="\" SET "SCRIPT_DIR=%SCRIPT_DIR:~0,-1%"

REM Determinar PROJECT_DIR de forma robusta:
REM - se o script estiver no diretório do projeto (contém pom.xml), use-o;
REM - caso contrário, assume-se que o projeto está duas pastas acima (ex: tomcat-server\bin scenario).
if exist "%SCRIPT_DIR%\pom.xml" (
    set "PROJECT_DIR=%SCRIPT_DIR%"
) else (
    pushd "%SCRIPT_DIR%\..\.." >nul 2>&1
    set "PROJECT_DIR=%CD%"
    popd >nul 2>&1
)

echo [1/4] Compilando projeto (pom em %PROJECT_DIR%)...
call mvn -f "%PROJECT_DIR%\pom.xml" clean package -DskipTests -q
if errorlevel 1 (
    echo ERRO: Falha na compilacao!
    pause
    exit /b 1
)

REM Localiza o WAR gerado
set "WAR_FILE="
for %%F in ("%PROJECT_DIR%\target\*.war") do set "WAR_FILE=%%~fF"

if not defined WAR_FILE (
    echo ERRO: Nenhum WAR encontrado em "%PROJECT_DIR%\target"
    pause
    exit /b 1
)

REM Determinar TOMCAT_BASE de forma robusta:
REM - se houver uma pasta tomcat-server no SCRIPT_DIR, use-a;
REM - se o script estiver dentro de tomcat-server\bin, o TOMCAT_BASE é o pai do bin.
if exist "%SCRIPT_DIR%\tomcat-server" (
    for %%X in ("%SCRIPT_DIR%\tomcat-server") do set "TOMCAT_BASE=%%~fX"
) else if exist "%SCRIPT_DIR%\..\webapps" (
    pushd "%SCRIPT_DIR%\.." >nul 2>&1
    set "TOMCAT_BASE=%CD%"
    popd >nul 2>&1
) else (
    REM fallback: assume tomcat base is SCRIPT_DIR
    set "TOMCAT_BASE=%SCRIPT_DIR%"
)

echo [2/4] Implantando aplicacao: %WAR_FILE%
copy /Y "%WAR_FILE%" "%TOMCAT_BASE%\webapps\" >nul

echo [3/4] Reiniciando Tomcat (stop -> start)...
cd /d "%TOMCAT_BASE%\bin"
call catalina.bat stop >nul 2>&1
timeout /t 2 /nobreak >nul
call catalina.bat start >nul 2>&1

echo [4/4] Verificando servidor...
timeout /t 3 /nobreak >nul

echo ========================================
echo  Servidor rodando em:
echo  http://localhost:8080/agenda-saas/
echo ========================================
echo.
echo Logs: %TOMCAT_BASE%\logs\catalina.*.log

ENDLOCAL
pause
