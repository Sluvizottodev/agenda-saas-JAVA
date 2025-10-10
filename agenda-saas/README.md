AgendaSaaS
===========

Como o sistema funciona (fluxo detalhado)
----------------------------------------
Este projeto segue um padrão simples baseado em Servlets, DAOs e JSPs. Abaixo está o ciclo completo de processamento de uma requisição típica:

1. Requisição HTTP
	- O usuário (navegador) acessa uma URL do sistema.
	- O servlet container (Tomcat) recebe a requisição e a roteia para o `Servlet` configurado em `web.xml` ou através de anotações.

2. Filtros e autenticação (não implementado ainda)
	- Antes do `Servlet` ser executado, filtros (ex.: `AuthFilter`, `RoleFilter`) podem interceptar a requisição para validar sessão, token ou permissões.

3. Servlet / Controller
	- O `Servlet` responsável (ex.: `AgendamentoServlet`, `ClienteServlet`) recupera parâmetros da requisição (`request.getParameter(...)`), valida entradas e constrói objetos de domínio quando necessário.
	- O `Servlet` delega a lógica de negócio para classes de `service` (quando presentes). Serviços encapsulam regras e orquestram chamadas a múltiplos DAOs quando preciso.

4. DAO (persistência)
	- As classes em `br.cefet.agendaSaas.dao` executam operações SQL usando JDBC.
	- Para obter conexão, os DAOs usam `ConnectionFactory.getConnection()` (que monta a URL e chama `DriverManager.getConnection(...)`).
	- Os DAOs usam `PreparedStatement` para prevenir SQL injection, mapeiam `ResultSet` para entidades e garantem fechamento de recursos via try-with-resources.

5. Banco de dados
	- O banco (MySQL) executa as queries e mantém integridade referencial (FKs). Os DAOs recebem os resultados e convertem em objetos Java.

6. Retorno ao Servlet
	- O `Servlet` recebe os objetos resultantes (ou flags de sucesso/falha) e decide a próxima etapa: redirecionar, forward para JSP ou retornar JSON.

7. View (JSP)
	- Quando o fluxo envolve renderização de páginas, o `Servlet` encaminha (faz forward via `RequestDispatcher`) para um JSP em `src/main/webapp` (ex.: `cliente/dashboardCliente.jsp`).
	- Os JSPs usam JSTL e EL para apresentar os dados (por exemplo, listas de agendamentos), e são renderizados pelo servlet container para HTML final.

8. Resposta ao cliente
	- O HTML gerado é enviado ao navegador. O usuário vê a página resultante e pode interagir novamente.

Como rodar localmente (rápido)
-----------------------------
1. Copie o template de variáveis e ajuste conforme seu ambiente:

```bash
cp .env.example .env
# editar .env com DB_USER/DB_PASSWORD e outros valores conforme seu ambiente local
```

2. Build e deploy (via Maven):

```bash
mvn clean package
# copie o WAR gerado para o Tomcat ou rode via IDE
```

Executando testes
-----------------
Os testes JUnit podem usar um banco local configurado nas variáveis de ambiente. Execute:

```bash
mvn test
```

