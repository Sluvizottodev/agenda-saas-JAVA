@echo off
REM update-webapp.bat - Atualiza apenas os arquivos web (JSP, CSS, JS)
SETLOCAL

echo ========================================
echo    ATUALIZANDO ARQUIVOS WEB
echo ========================================

echo [1/2] Copiando arquivos web atualizados...

REM Copiar arquivos JSP e recursos web
xcopy /Y /E "src\main\webapp\*" "tomcat-server\webapps\agenda-saas\" >nul 2>&1

echo [2/2] Arquivos atualizados com sucesso!
echo.
echo ✅ As mudanças em JSP, CSS e JS já estão disponíveis em:
echo    http://localhost:8080/agenda-saas/
echo.
echo 💡 Para mudanças em código Java, use: start-server.bat
echo.

ENDLOCAL
pause