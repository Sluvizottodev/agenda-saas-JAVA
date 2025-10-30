# 📅 Agenda SaaS

Aplicação para gerenciamento de agendas e compromissos, desenvolvida em Java
com JSP/Servlets e empacotada como WAR para execução em Apache Tomcat.

---

## 🧾 Principais funcionalidades
- Cadastro e autenticação de usuários
- Agendamento e gerenciamento de compromissos
- Listagem de horários

---

## 🛠️ Tecnologias
- Java 21
- Maven
- Jakarta Servlet/JSP
- Hibernate / JPA
- MySQL 8
- Apache Tomcat (incluso no repositório em `agenda-saas/tomcat-server`)
```

---

## Pré-requisitos
- JDK 21 (ou versão compatível com o projeto)
- Maven (apache-maven)
- MySQL 8 (ou acesso a um servidor MySQL compatível)

Observação: este repositório inclui um Tomcat local em `agenda-saas/tomcat-server` para facilitar testes.

---

## Build e execução (rápido)

1) Build (na raiz do repositório):

```cmd
cd /d C:\Users\User\DEV\agenda-saas
mvn -f agenda-saas\pom.xml clean package
```

2) Deploy manual (opcional): copie `target/agenda-saas.war` para `agenda-saas/tomcat-server/webapps` e inicie o Tomcat:

```cmd
copy agenda-saas\target\agenda-saas.war agenda-saas\tomcat-server\webapps\agenda-saas.war
call agenda-saas\tomcat-server\bin\startup.bat
```

3) Acessar a aplicação:

```
http://localhost:8080/agenda-saas/
```

---

## Script de conveniência: run-dev.bat

Se preferir um passo único para compilar e fazer o deploy local, há um `run-dev.bat` na raiz do repositório. Ele faz:

- mvn clean package -DskipTests
- copia o WAR gerado para `agenda-saas/tomcat-server/webapps`
- limpa o diretório explodido (se existente) e caches (`work`, `temp`)
- inicia o Tomcat local (`tomcat-server/bin/startup.bat`)

Como usar:

```cmd
cd /d C:\Users\User\DEV\agenda-saas
run-dev.bat
```

Após a execução, verifique logs em `agenda-saas/tomcat-server/logs/tomcat-console.log`.

---

## Banco de dados

- O projeto inclui `agenda-saas/init-database.sql` e `database-schema.sql` com scripts de criação/seed.
- Atualize as credenciais em `src/main/resources/META-INF/persistence.xml` conforme seu ambiente.

Credenciais de desenvolvimento (padrão usado nos exemplos):

- usuário: `root`
- senha: `rootpass`
- banco: `agenda_saas`

Se preferir, execute os scripts SQL manualmente no seu MySQL antes de iniciar a aplicação.

---

## Variáveis de ambiente (.env)

O projeto também aceita configuração por variáveis de ambiente — o código que abre conexões JDBC (classe `ConnectionFactory`) lê as variáveis abaixo usando `System.getenv(...)`:

- DB_HOST (padrão: localhost)
- DB_PORT (padrão: 3306)
- DB_NAME (padrão: agenda_saas)
- DB_USER (padrão: root)
- DB_PASSWORD (padrão: vazio)

Existe um arquivo de exemplo em `.env.example` na raiz do repositório. Para uso local, copie esse arquivo para `.env` e preencha valores sensíveis; o `run-dev.bat` e os scripts de desenvolvimento irão carregar automaticamente esse `.env` quando presente. Recomendamos o fluxo:

- Na raiz do repositório, execute:

```bat
copy .env.example .env
```

Isso cria um `.env` local que NÃO deve ser comitado (já está no `.gitignore`). O `run-dev.bat` carregará as variáveis do `.env` ao iniciar, e o mesmo `.env` funciona tanto para o Docker quanto para execução local (as variáveis definem as credenciais que o container e a aplicação utilizarão).

Se preferir não usar `run-dev.bat`, você também pode exportar as variáveis manualmente antes de iniciar o Tomcat/Java.


Fallback e `persistence.xml`:
- O `persistence.xml` presente em `src/main/resources/META-INF/persistence.xml` contém valores de exemplo e é usado como fallback. Se preferir, mantenha as credenciais no `persistence.xml` para ambientes simples, mas em produção use variáveis de ambiente ou configuração segura.

Integração com `run-dev.bat` / Docker:
- O `run-dev.bat` na raiz contém lógica para montar um MySQL em Docker e exportar variáveis de ambiente para a execução local de desenvolvimento. Se você usa esse script, ele já define as variáveis `DB_HOST`, `DB_PORT`, `DB_USER`, `DB_PASSWORD` antes de iniciar o container e a aplicação.

## CRUD por entidade (mapa rápido)

A seguir há um resumo dos principais modelos (entidades) do sistema e onde cada operação CRUD está implementada — isso ajuda a entender como criar/ler/atualizar/remover registros no projeto.

- Cliente
	- Entidade: `br.cefet.agendasaas.model.entidades.Cliente`
	- Create: `ClienteDAO.inserir(Cliente)` (usado por `ClienteService.cadastrar`) e via formulário de cadastro (`/auth/cadastro` — `CadastroServlet`) com página `src/main/webapp/auth/cadastro.jsp`.
	- Read: `ClienteDAO.buscarPorId`, `ClienteDAO.listarTodos` (acessíveis via `ClienteService`).
	- Update: `ClienteDAO.atualizar` (via `ClienteService.atualizar`).
	- Delete: `ClienteDAO.remover` (via `ClienteService.remover`).

- Prestador (usuário do tipo prestador)
	- Entidade: `br.cefet.agendasaas.model.entidades.Prestador`
	- Create: suporte via `CadastroServlet` (inserção JDBC) e também operações via `PrestadorService` que usa `GenericDAO` (`save`). Página de cadastro: `auth/cadastro.jsp`.
	- Read: `PrestadorService.buscarPorId` / `listarTodos`. Dashboard do prestador: `DashboardPrestadorServlet` (`/dashboard/prestador`) e view em `WEB-INF/views/dashboardPrestador.jsp`.
	- Update: `PrestadorService.atualizar` (usa `GenericDAO.update`).
	- Delete: `PrestadorService.remover` (usa `GenericDAO.deleteById`).

- Serviço
	- Entidade: `br.cefet.agendasaas.model.entidades.Servico`
	- Create: `ServicoDAO.inserir(Servico)` (acessível por `ServicoService.cadastrar`).
	- Read: `ServicoDAO.buscarPorId`, `ServicoDAO.listarTodos`, `ServicoDAO.listarPorPrestador` (usado por `AgendamentoServlet` para preencher `/agendar`).
	- Update: `ServicoDAO.atualizar` (via `ServicoService.atualizar`).
	- Delete: `ServicoDAO.remover` (via `ServicoService.remover`).

- Horário Disponível
	- Entidade: `br.cefet.agendasaas.model.entidades.HorarioDisponivel`
	- Create: `HorarioDisponivelDAO.inserir` (via `HorarioService.cadastrarHorario`).
	- Read: várias consultas: `listarPorPrestador`, `listarDisponiveisPorPrestador`, `listarPorPrestadorEData`, `listarPorPeriodo`.
	- Update: `HorarioDisponivelDAO.atualizar` (via `HorarioService.atualizarHorario`).
	- Delete: `HorarioDisponivelDAO.remover` e `removerPorPrestador` (via `HorarioService.removerHorario`).
	- Endpoints/Views: `HorarioServlet` mapeado em `/horarios`, `/horarios/cadastrar`, `/horarios/editar`, `/horarios/remover`, `/horarios/gerar` e views em `WEB-INF/views/horarios/` (ex.: `listar.jsp`, `cadastrar.jsp`, `editar.jsp`, `gerar.jsp`).

- Agendamento
	- Entidade: `br.cefet.agendasaas.model.entidades.Agendamento`
	- Create: `AgendamentoDAO.inserir` (Chamado por `AgendamentoService.agendar` / `AgendamentoServlet` — POST em `/agendar`).
	- Read: `AgendamentoDAO.listarPorPrestador`, `AgendamentoDAO.listarPorCliente` (usado em `/agendamentos`).
	- Update: `AgendamentoDAO.update` / `AgendamentoService.atualizar`.
	- Delete (cancelar): `AgendamentoDAO.remover` (via `AgendamentoService.cancelar`).
	- Views: formulário de agendamento `WEB-INF/views/agendar.jsp` e `WEB-INF/views/agendamento-sucesso.jsp`.

- Pagamento
	- Entidade: `br.cefet.agendasaas.model.entidades.Pagamento`
	- Implementação de persistência: `PagamentoService` usa `GenericDAO<Pagamento,Integer>` (CRUD básico: criar/buscar/atualizar/remover via `GenericDAO`). Observação: `PagamentoDAO` está presente mas vazio; a lógica atual fica em `PagamentoService` + `GenericDAO`.

- Notificação
	- Entidade: `br.cefet.agendasaas.model.entidades.Notificacao`
	- Persistência: `NotificacaoService` usa `GenericDAO<Notificacao,Integer>` (criar/buscar/listar/atualizar/remover via `GenericDAO`).

- Usuário (abstrato) / Autenticação
	- Entidade base: `br.cefet.agendasaas.model.entidades.Usuario` (subclasses `Cliente` e `Prestador`).
	- Cadastro: via `CadastroServlet` (`/auth/cadastro`) e view `auth/cadastro.jsp`.
	- Login: `LoginServlet` (`/auth/login`) utiliza `UsuarioDAO.buscarPorEmailSenha`.
	- Operações CRUD genéricas: `UsuarioDAO` usa `GenericDAO<Usuario,Integer>` para operações básicas (`save`, `findById`, `update`, `deleteById`).

- Camadas:
	- DAO: acesso direto ao banco (JDBC ou JPA).
	- Service: regras de negócio e validações (`*Service` classes).
	- Controller/Servlet: mapeamento de URL e apresentação (JSP). Exemplos: `HorarioServlet`, `AgendamentoServlet`, `CadastroServlet`, `LoginServlet`.

