@echo off
setlocal

echo ========================================
echo    AGENDA-SaaS - Automação Dev (Docker)
echo ========================================

rem --- checar Docker
docker info >nul 2>&1
if errorlevel 1 (
  echo ERRO: Docker daemon nao esta ativo. Abra o Docker Desktop e execute novamente.
  exit /b 1
)

rem --- variaveis
set DB_ROOT_PASS=rootpass
set APP_DB_USER=agenda
set APP_DB_PASS=rootpass
set DB_NAME=agenda_saas
set CONTAINER_NAME=agenda-mysql
set MYSQL_IMAGE=mysql:8.0

echo Removendo container antigo (se existir)...
docker rm -f %CONTAINER_NAME% >nul 2>&1

echo Iniciando container MySQL (%MYSQL_IMAGE%)...
docker run -d --name %CONTAINER_NAME% -e MYSQL_ROOT_PASSWORD=%DB_ROOT_PASS% -p 3306:3306 %MYSQL_IMAGE% >nul

echo Aguardando MySQL iniciar (aguarde até 30s)...
set ATTEMPTS=0
:waitloop
set /a ATTEMPTS+=1
docker exec %CONTAINER_NAME% mysqladmin ping -uroot -p%DB_ROOT_PASS% >nul 2>&1
if %errorlevel%==0 (
  echo MySQL ativo (tentativa %ATTEMPTS%)
  goto :importdb
)
if %ATTEMPTS% GEQ 30 (
  echo ERRO: Timeout aguardando MySQL iniciar.
  exit /b 1
)
timeout /t 1 >nul
goto :waitloop

:importdb
echo Forcando importacao do arquivo init-database.sql para %CONTAINER_NAME% (tentativas)...
set IMPORT_ATTEMPTS=0
set IMPORT_OK=0
for /l %%j in (1,1,20) do (
  set /a IMPORT_ATTEMPTS+=1
  echo Tentativa %%j de importacao...
  type init-database.sql | docker exec -i %CONTAINER_NAME% sh -c "mysql -uroot -p%DB_ROOT_PASS%" >nul 2>&1
  if %errorlevel%==0 (
    set IMPORT_OK=1
    echo Importacao concluida na tentativa %%j
    goto :after_import
  )
  timeout /t 1 >nul
)
echo Aviso: importacao nao concluida apos %IMPORT_ATTEMPTS% tentativas.

:after_import

echo Criando usuario de aplicacao '%APP_DB_USER%' e concedendo privilegios...
docker exec -i %CONTAINER_NAME% sh -c "mysql -uroot -p%DB_ROOT_PASS% -e \"CREATE USER IF NOT EXISTS '%APP_DB_USER%'@'%' IDENTIFIED WITH mysql_native_password BY '%APP_DB_PASS%'; GRANT ALL PRIVILEGES ON %DB_NAME%.* TO '%APP_DB_USER%'@'%'; FLUSH PRIVILEGES;\""

echo Setando variaveis de ambiente para sessao atual...
set DB_HOST=localhost
set DB_PORT=3306
set DB_NAME=%DB_NAME%
set DB_USER=%APP_DB_USER%
set DB_PASSWORD=%APP_DB_PASS%

echo Buildando o projeto (mvn package)...
mvn -DskipTests package

echo Iniciando a aplicacao via start-server.bat...
call start-server.bat

echo Aguardando aplicacao responder em http://localhost:8080/agenda-saas/index.jsp ...
for /l %%i in (1,1,40) do (
  powershell -Command "try { Invoke-WebRequest -UseBasicParsing http://localhost:8080/agenda-saas/index.jsp -TimeoutSec 3 | Out-Null; exit 0 } catch { exit 1 }"
  if errorlevel 1 (
    timeout /t 1 >nul
  ) else (
    echo Aplicacao disponivel.
    goto :done
  )
)

echo ERRO: A aplicacao nao respondeu no tempo esperado.
exit /b 1

:done
echo ==================================================
echo Ambiente de desenvolvimento pronto.
echo DB host=%DB_HOST% port=%DB_PORT% db=%DB_NAME%
echo App DB user=%DB_USER% password=%DB_PASSWORD%
echo Acesse: http://localhost:8080/agenda-saas/index.jsp
echo ==================================================

endlocal
exit /b 0
