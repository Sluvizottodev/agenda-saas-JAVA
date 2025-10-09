# AgendaSaaS

Sistema de agendamento de serviços desenvolvido em Java com Maven e Tomcat.

## 🚀 Como Executar

1. **Executar o servidor:**
   ```bash
   ./start-server.bat
   ```

2. **Acessar a aplicação:**
   - URL: http://localhost:8080/agenda-saas/

## 🛠️ Desenvolvimento

### Comandos Maven:
```bash
# Compilar projeto
mvn clean compile

# Gerar WAR
mvn clean package

# Limpar build
mvn clean
```

### VS Code Tasks:
- `Ctrl+Shift+P` → `Tasks: Run Task`
- **Build AgendaSaaS** - Compila o projeto
- **Start Server** - Inicia o servidor Tomcat
- **Clean Project** - Limpa arquivos de build

## 📋 Estrutura do Projeto

```
agenda-saas/
├── src/main/java/           # Código fonte Java
├── src/main/webapp/         # Arquivos web (JSP, CSS, JS)
├── tomcat-server/           # Servidor Tomcat local
├── database-schema.sql      # Schema do banco de dados
├── init-database.sql        # Script de inicialização do BD
└── start-server.bat         # Script para executar o projeto
```

## 🗃️ Banco de Dados

- **SGBD:** MySQL
- **Port:** 3306
- **Database:** agenda_saas
- **Executar:** `init-database.sql` para criar tabelas e dados de teste

## ⚡ Desenvolvimento Rápido

Para mudanças em código Java:
1. Modifique o código
2. Execute `start-server.bat` (recompila automaticamente)
3. Acesse http://localhost:8080/agenda-saas/

Para mudanças em JSP/CSS/JS:
- São detectadas automaticamente pelo Tomcat