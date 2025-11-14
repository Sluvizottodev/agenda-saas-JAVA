@echo off
setlocal enabledelayedexpansion

echo ========================================
echo    AGENDA-SaaS - Ambiente Dev (Docker)
echo ========================================

rem --- Checar Docker
docker info >nul 2>&1
if errorlevel 1 (
  echo [ERRO] Docker daemon nao esta ativo. Abra o Docker Desktop e execute novamente.
  exit /b 1
)

rem --- Variaveis principais
set DB_ROOT_PASS=rootpass
set APP_DB_USER=agenda
set APP_DB_PASS=rootpass
set DB_NAME=agenda_saas
set CONTAINER_NAME=agenda-mysql
set MYSQL_IMAGE=mysql:8.0

rem --- Detecta diretorio do script
set SCRIPT_DIR=%~dp0

rem --- Se existir .env na pasta do script, carregue as variaveis
if exist "%SCRIPT_DIR%\.env" (
  echo Carregando variaveis de ambiente de %SCRIPT_DIR%\.env
  for /f "usebackq tokens=1* delims==" %%A in ("%SCRIPT_DIR%\.env") do (
    set "_KEY=%%A"
    set "_VAL=%%B"
    if not "!_KEY:~0,1!"=="#" (
      set "%%A=%%B"
    )
  )
)

echo Removendo container antigo (se existir)...
docker rm -f %CONTAINER_NAME% >nul 2>&1

echo Iniciando container MySQL (%MYSQL_IMAGE%) com healthcheck...
docker run -d --name %CONTAINER_NAME% ^
  -e MYSQL_ROOT_PASSWORD=%DB_ROOT_PASS% ^
  -p 3306:3306 ^
  --health-cmd "mysqladmin ping -uroot -p%DB_ROOT_PASS%" ^
  --health-interval 5s ^
  --health-timeout 2s ^
  --health-retries 20 ^
  %MYSQL_IMAGE% >nul

echo Aguardando MySQL iniciar (health=healthy, ate ~120s)...
set ATTEMPTS=0
:wait_mysql
set /a ATTEMPTS+=1
for /f "usebackq tokens=*" %%S in (`docker inspect --format "{{.State.Health.Status}}" %CONTAINER_NAME% 2^>nul`) do set HEALTH=%%S
echo Status do health: %HEALTH% (tentativa %ATTEMPTS%)
if "%HEALTH%"=="healthy" (
  echo MySQL ativo (tentativa %ATTEMPTS%)
  goto :importdb
)
if %ATTEMPTS% GEQ 24 (
  echo [ERRO] Timeout aguardando MySQL ficar healthy.
  docker logs --tail 50 %CONTAINER_NAME%
  exit /b 1
)
timeout /t 5 >nul
goto :wait_mysql

:importdb
echo Localizando arquivo SQL de inicializacao...

rem --- Prioridade: init-database.sql, depois database-schema.sql
if exist "%SCRIPT_DIR%init-database.sql" (
  set SQL_FILE=%SCRIPT_DIR%init-database.sql
) else if exist "%SCRIPT_DIR%database-schema.sql" (
  set SQL_FILE=%SCRIPT_DIR%database-schema.sql
) else (
  echo [ERRO] Nenhum arquivo init-database.sql ou database-schema.sql encontrado.
  exit /b 1
)

echo Importando esquema e dados (via docker cp)...
timeout /t 3 >nul

rem Copiar arquivo SQL para dentro do container para evitar problemas de pipe/CRLF
docker cp "%SQL_FILE%" %CONTAINER_NAME%:/tmp/init-database.sql >nul 2>&1
echo Arquivo SQL copiado para o container.

set IMPORT_ATTEMPT=1
:import_loop
echo Tentativa %IMPORT_ATTEMPT% de importacao (arquivo dentro do container)...
docker exec %CONTAINER_NAME% sh -c "mysql -uroot -p%DB_ROOT_PASS% < /tmp/init-database.sql"
echo ExitCode import: %ERRORLEVEL%
if %ERRORLEVEL%==0 (
  echo Importacao concluida (tentativa %IMPORT_ATTEMPT%)
  goto :after_import
)
set /a IMPORT_ATTEMPT+=1
if %IMPORT_ATTEMPT% LEQ 10 (
  timeout /t 2 >nul
  goto :import_loop
)
echo [AVISO] Importacao falhou apos 10 tentativas.

:after_import
echo Criando usuario de aplicacao '%APP_DB_USER%'...
docker exec -i %CONTAINER_NAME% sh -c "mysql -uroot -p%DB_ROOT_PASS% -e \"CREATE DATABASE IF NOT EXISTS %DB_NAME%; CREATE USER IF NOT EXISTS '%APP_DB_USER%'@'%%' IDENTIFIED WITH mysql_native_password BY '%APP_DB_PASS%'; GRANT ALL PRIVILEGES ON %DB_NAME%.* TO '%APP_DB_USER%'@'%%'; FLUSH PRIVILEGES;\""

rem --- Variaveis de ambiente
set DB_HOST=localhost
set DB_PORT=3306
set DB_NAME=%DB_NAME%
set DB_USER=%APP_DB_USER%
set DB_PASSWORD=%APP_DB_PASS%

echo Compilando projeto (mvn package)...
mvn -f "%SCRIPT_DIR%pom.xml" -DskipTests package
if errorlevel 1 (
  echo [ERRO] Falha no build Maven.
  exit /b 1
)

rem --- Copia WAR para Tomcat
set WAR_FILE=
for %%F in ("%SCRIPT_DIR%target\*.war") do set WAR_FILE=%%~fF
if not defined WAR_FILE (
  echo [ERRO] Nenhum arquivo WAR encontrado.
  exit /b 1
)
copy /Y "%WAR_FILE%" "%SCRIPT_DIR%tomcat-server\webapps\" >nul

echo Reiniciando Tomcat...
pushd "%SCRIPT_DIR%tomcat-server\bin"
call catalina.bat stop >nul 2>&1
timeout /t 1 >nul
call catalina.bat start >nul 2>&1
popd

echo ==================================================
echo Ambiente de desenvolvimento pronto.
echo DB: %DB_NAME% em %DB_HOST%:%DB_PORT%
echo Usuario: %DB_USER% | Senha: %DB_PASSWORD%
echo Acesse: http://localhost:8080/agenda-saas/index.jsp
echo ==================================================
endlocal
exit /b 0
