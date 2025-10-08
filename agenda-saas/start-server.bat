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

REM Copiar WAR
echo [2/3] Implantando aplicacao...
copy /Y "target\agenda-saas.war" "tomcat-server\webapps\" >nul

REM Iniciar Tomcat
echo [3/3] Iniciando servidor...
echo.
echo ========================================
echo  Servidor rodando em:
echo  http://localhost:8080/agenda-saas/
echo ========================================
echo.
echo Pressione Ctrl+C para parar o servidor
echo.

cd tomcat-server\bin
call catalina.bat run

ENDLOCAL