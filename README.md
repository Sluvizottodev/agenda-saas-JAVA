# 📅 Agenda SaaS - Spring Boot API

API REST desenvolvida com Spring Boot para gerenciamento de agendas e compromissos com autenticação JWT.

---

## 🧾 Principais funcionalidades
- **API REST completa** com endpoints JSON
- **Autenticação JWT** com Spring Security
- **Cadastro e login** de usuários (Clientes e Prestadores)
- **Endpoints protegidos** com autenticação Bearer Token
- **Agendamento e gerenciamento** de compromissos
- **Listagem de horários disponíveis**
- **CRUD completo** para todas as entidades
- **Validações robustas** em todas as operações

---

## 🛠️ Tecnologias
- **Java 21**
- **Spring Boot 3.2.12**
- **Spring Security 6**
- **JWT (jsonwebtoken 0.12.3)**
- **Spring Data JPA**
- **Spring Web (REST)**
- **Maven**
- **Hibernate / JPA**
- **H2 Database** (desenvolvimento)
- **MySQL 8** (produção)
- **CORS configurado** para frontend

---

## 📋 Pré-requisitos
- **JDK 21** (ou versão compatível)
- **Maven** (apache-maven)
- **MySQL 8** (ou acesso a um servidor MySQL compatível)

---

## 🚀 Execução Rápida

```bash
# Compilar e executar
cd agenda-saas
mvn clean compile
mvn spring-boot:run
```

A API estará disponível em: **http://localhost:8080/api**

**Database**: H2 (em memória) - Console: http://localhost:8080/api/h2-console

---

## 🔐 Autenticação JWT

### **Endpoints de Autenticação**
- `POST /api/auth/register/cliente` - Cadastrar cliente
- `POST /api/auth/register/prestador` - Cadastrar prestador  
- `POST /api/auth/login` - Login (retorna JWT)
- `GET /api/auth/me` - Dados do usuário autenticado
- `POST /api/auth/logout` - Logout

### **Como usar JWT**
1. Fazer login e receber o token
2. Incluir nos headers: `Authorization: Bearer {token}`
3. Acessar endpoints protegidos

### **Exemplo de Cadastro**
```json
POST /api/auth/register/cliente
{
  "nome": "João Silva",
  "cpf": "12345678901",
  "cnpj": "12345678000195", 
  "email": "joao@teste.com",
  "telefone": "(11) 99999-9999",
  "senha": "123456"
}
```

### **Exemplo de Login**
```json
POST /api/auth/login
{
  "email": "joao@teste.com",
  "senha": "123456"
}
```

---

## 📚 Principais Endpoints

### **Públicos (sem autenticação)**
- `POST /api/auth/register/cliente` - Cadastro cliente
- `POST /api/auth/register/prestador` - Cadastro prestador
- `POST /api/auth/login` - Login
- `GET /api/postman/status` - Status da API

### **Protegidos (requer JWT)**
- `GET /api/auth/me` - Dados do usuário logado
- `GET /api/clientes` - Listar clientes
- `GET /api/prestadores` - Listar prestadores
- `GET /api/servicos` - Listar serviços  
- `GET /api/agendamentos` - Listar agendamentos
- `GET /api/horarios` - Listar horários

**Todos os endpoints CRUD** seguem o padrão REST (GET, POST, PUT, DELETE)

---

## 🏗️ Arquitetura

```
br.cefet.agendasaas/
├── controller/          # Endpoints REST
├── service/            # Lógica de negócio  
├── repository/         # Acesso a dados
├── security/           # JWT + Spring Security
├── dto/               # Transfer Objects
├── model/entidades/   # Entidades JPA
└── config/           # Configurações
```

---

## 📋 Principais Entidades

### **Cliente/Prestador** (herdam de Usuario)
```json
{
  "id": 1,
  "nome": "João Silva", 
  "email": "joao@email.com",
  "cpf": "12345678901",
  "telefone": "(11) 99999-9999",
  "senha": "hash_bcrypt"
}
```

### **Agendamento**
```json
{
  "id": 1,
  "dataHora": "2025-11-20T14:30:00",
  "status": "AGENDADO",
  "clienteId": 1,
  "prestadorId": 1,
  "servicoId": 1
}
```

---

## 📝 Notas

- **JWT expira em 24h** (configurável)
- **Senhas criptografadas** com BCrypt
- **H2 Database** para desenvolvimento rápido
- **CORS liberado** para qualquer origem
- **Spring Security** protege endpoints automaticamente

---

## 🐛 Troubleshooting

**Porta em uso**: Altere no `application.properties`
```properties
server.port=8081
```

**Erro JWT**: Verifique o header Authorization:
```
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

