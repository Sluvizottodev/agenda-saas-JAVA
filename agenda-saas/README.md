Agenda SaaS
===========

Aplicação para gerenciamento de agendas e compromissos, desenvolvida em Java com Maven, JSP/Servlets e deploy em Apache Tomcat.

Resumo rápido
-------------
- Cadastro e autenticação de usuários
- Criação, edição e exclusão de compromissos
- Listagem organizada de compromissos
- Interface web baseada em JSP

Requisitos mínimos
------------------
- Java 11+
- Maven
- Apache Tomcat 9+ (ou execução via IDE)

Estrutura principal
-------------------
```
agenda-saas/
 ├── src/main/java/...   # Código fonte (servlets, controllers, models, daos)
 ├── src/main/webapp/    # JSPs, CSS, JS
 ├── src/test/           # Testes JUnit
 ├── init-database.sql   # Script para esquema
 ├── pom.xml             # Build Maven
 └── README.md
```

Instalação e execução rápida
---------------------------
1. Clone o repositório e entre na pasta:

```bash
git clone https://github.com/Sluvizottodev/agenda-saas-JAVA.git
cd agenda-saas
```

2. Configure as variáveis de ambiente (ou copie `.env.example` para `.env`):

```bash
cp .env.example .env
# Edite .env para apontar para seu banco local
3. Compile e empacote:

```bash
mvn clean package
```

4. Faça o deploy do WAR no Tomcat (ou rode a aplicação via IDE) e acesse:

```
http://localhost:8080/agenda-saas
```

Como o sistema funciona (fluxo)
------------------------------
O ciclo de uma requisição segue estas etapas:

1) Recepção
- O navegador faz uma requisição HTTP.
- O servidor (Tomcat) encaminha para o `Servlet` configurado para a rota.

2) Filtros (opcional)
- Filtros como autenticação ou autorização podem interceptar e validar a requisição antes do servlet.

3) Servlet / Controller
- O servlet lê parâmetros (`request.getParameter(...)`), valida e chama serviços/DAOs.

4) Camada de persistência (DAO)
- Os DAOs executam queries via JDBC e mapeiam resultados para entidades Java.
- Use sempre `PreparedStatement` e try-with-resources para evitar leaks.

5) Resposta / View
- O servlet decide a resposta: JSON, redirect, ou renderização de JSP.
- Para renderizar uma página, o servlet geralmente encaminha para o JSP usando `RequestDispatcher.forward` (ex.: `request.getRequestDispatcher("/cliente/dashboardCliente.jsp").forward(request, response);`).
- Para redirecionar o navegador para outra URL, use `response.sendRedirect(...)`.

6) Retorno ao cliente
- HTML/JSON é enviado ao cliente e o ciclo se repete conforme interações do usuário.

Testes
------
Execute os testes com:

```bash
mvn test
```

