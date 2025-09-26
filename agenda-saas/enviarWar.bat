@echo off
REM Script para mover o arquivo WAR da pasta target para o Tomcat

REM Caminho do WAR (arquivo)
set "origem=C:\Users\Aluno\Sl\arq\agenda-saas\target\agenda-saas.war"

REM Caminho da pasta de destino (Tomcat webapps)
set "destino=C:\dev\pac2025\tomcat\webapps"

REM Cria a pasta de destino se não existir
if not exist "%destino%" (
    mkdir "%destino%"
)

REM Move o arquivo WAR para o destino
move /Y "%origem%" "%destino%"

REM Mensagem de confirmação
if %errorlevel%==0 (
    echo WAR movido com sucesso para %destino%!
) else (
    echo ERRO: Arquivo não encontrado ou erro ao mover.
)

pause
