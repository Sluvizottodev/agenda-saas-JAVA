# 📅 Agenda SaaS

Aplicação **SaaS (Software as a Service)** para gerenciamento de agendas e compromissos.  
Desenvolvida em **Java**, utilizando **Maven**, **JSP/Servlets** e **Apache Tomcat**.

---

## 🧾 Funcionalidades
- 🔐 Cadastro e autenticação de usuários  
- 📌 Criação, edição e exclusão de compromissos  
- 📋 Listagem de compromissos organizados  
- 🌐 Interface web com JSP  
- 🚀 Deploy em servidor **Apache Tomcat**

---

## 🛠️ Tecnologias Utilizadas
- ☕ **Java 11+**
- 📦 **Maven**
- 🖥️ **JSP/Servlets**
- 🗄️ **MySQL**
- 🐱‍🏍 **Apache Tomcat 9+**

---

## 📂 Estrutura do Projeto
```
agenda-saas/
 ├── src/main/java/...   # Código fonte (Servlets, Controllers, Models)
 ├── src/main/webapp/    # Arquivos JSP, CSS e JS
 ├── pom.xml             # Configuração do Maven
 └── README.md           # Documentação
```

---

## ⚙️ Como Executar

### 🔹 1. Pré-requisitos
- [Java 11+](https://adoptopenjdk.net/)  
- [Maven](https://maven.apache.org/)  
- [Apache Tomcat 9+](https://tomcat.apache.org/) ou extensão **Tomcat for Java** no VS Code  

### 🔹 2. Clonar o Repositório
```bash
git clone https://github.com/Sluvizottodev/agenda-saas-JAVA.git
cd agenda-saas
```

### 🔹 3. Compilar e Empacotar
```bash
mvn clean package
```

Gera o arquivo:  
```
target/agenda-saas.war
```

### 🔹 4. Acessar no Navegador
```
http://localhost:8080/agenda-saas
```

---

## 🏃‍♂️ Ambiente de Desenvolvimento (automação)

Para facilitar o desenvolvimento local, há um script que automatiza a criação
de um banco MySQL em container Docker, importa o script de inicialização
(`init-database.sql`), cria um usuário de aplicação e inicia o servidor Tomcat
com a aplicação.

Arquivo: `run-dev.bat` (na raiz do projeto)

O que o `run-dev.bat` faz:
- Verifica se o Docker Desktop/daemon está ativo
- Remove (se existir) e cria um container MySQL (`agenda-mysql`) com `mysql:8.0`
- Aguarda o MySQL ficar pronto e importa `init-database.sql`
- Cria usuário de aplicação `agenda` com permissões no schema `agenda_saas`
- Define variáveis de ambiente temporárias para a sessão e executa `mvn package`
- Executa `start-server.bat` para redeploy e aguarda a aplicação responder

Como usar (cmd.exe):

```cmd
cd /d C:\Users\User\DEV\agenda-saas\agenda-saas
run-dev.bat
```

Ao final você verá a URL de acesso: `http://localhost:8080/agenda-saas/index.jsp`

Comandos úteis:

```cmd
:: Parar e remover o container de desenvolvimento
docker stop agenda-mysql
docker rm agenda-mysql

:: Parar Tomcat (se iniciado manualmente)
tomcat-server\bin\shutdown.bat

:: Limpar build
mvn clean
```