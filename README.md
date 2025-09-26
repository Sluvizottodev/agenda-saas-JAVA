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
git clone https://github.com/seu-usuario/agenda-saas.git
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
