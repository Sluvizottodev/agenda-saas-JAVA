@echo off
REM start-server.bat - Inicia o AgendaSaaS
SETLOCAL

title AgendaSaaS Server

echo ========================================
echo         AGENDA SAAS - SERVIDOR
echo ========================================

REM Compilar e empacotar
echo [1/3] Compilando projeto...
call mvn clean package -DskipTests -q
if errorlevel 1 (
    echo ERRO: Falha na compilacao
    pause
    exit /b 1
)

@echo off
REM start-server.bat - Inicia o AgendaSaaS (robusto, caminhos relativos)
SETLOCAL

title AgendaSaaS Server

echo ========================================
echo         AGENDA SAAS - SERVIDOR
echo ========================================

REM determina o diretório do script
SET SCRIPT_DIR=%~dp0
IF "%SCRIPT_DIR:~-1%"=="\" SET SCRIPT_DIR=%SCRIPT_DIR:~0,-1%

cd /d "%SCRIPT_DIR%"

echo [1/4] Compilando projeto...
call mvn -f "%SCRIPT_DIR%\pom.xml" clean package -DskipTests -q
if errorlevel 1 (
    echo ERRO: Falha na compilacao
    pause
    exit /b 1
)

REM procurar o WAR no target (suporta nomes diferentes)
set WAR_FILE=
for %%F in ("%SCRIPT_DIR%\target\*.war") do set WAR_FILE=%%~fF

if not defined WAR_FILE (
    echo ERRO: Nenhum WAR encontrado em "%SCRIPT_DIR%\target"
    pause
    exit /b 1
)

echo [2/4] Implantando aplicacao: %WAR_FILE%
copy /Y "%WAR_FILE%" "%SCRIPT_DIR%\tomcat-server\webapps\" >nul

echo [3/4] Reiniciando Tomcat (stop -> start)...
cd /d "%SCRIPT_DIR%\tomcat-server\bin"
call catalina.bat stop >nul 2>&1
timeout /t 1 /nobreak >nul
call catalina.bat start >nul 2>&1

echo [4/4] Verificando servidor...
echo Servidor (esperando alguns segundos para iniciar)...
timeout /t 3 /nobreak >nul

echo ========================================
echo  Servidor rodando em:
echo  http://localhost:8080/agenda-saas/
echo ========================================
echo.
echo Se quiser ver logs: %SCRIPT_DIR%\tomcat-server\logs\catalina.*.log

ENDLOCAL