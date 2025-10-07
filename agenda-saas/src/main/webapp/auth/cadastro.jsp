<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>Cadastro - AgendaSaaS</title>
    <link rel="stylesheet" href="../style.css">
    <style>
        .form-container {
            max-width: 600px;
            margin: 40px auto;
            background: #fff;
            padding: 30px;
            border-radius: 8px;
            box-shadow: var(--sombra-padrao);
        }

        .form-group {
            margin-bottom: 15px;
        }

        .form-group label {
            display: block;
            font-weight: 600;
            margin-bottom: 5px;
        }

        .form-group input, .form-group select {
            width: 100%;
            padding: 10px;
            border: 1px solid #ccc;
            border-radius: 6px;
        }

        .form-actions {
            text-align: center;
            margin-top: 20px;
        }

        .form-actions button {
            width: 100%;
        }

        .hidden {
            display: none;
        }
    </style>
    <script>
        function toggleCampos() {
            const tipo = document.getElementById("tipo").value;
            document.getElementById("campos-cliente").style.display = (tipo === "cliente") ? "block" : "none";
            document.getElementById("campos-prestador").style.display = (tipo === "prestador") ? "block" : "none";
        }

        // Executa ao carregar a página (para manter campos visíveis após reload, se necessário)
        window.addEventListener("DOMContentLoaded", toggleCampos);
    </script>
</head>
<body>
<header>
    <h1>AgendaSaaS</h1>
    <p>Cadastre-se como Cliente ou Prestador</p>
</header>

<main>
    <div class="form-container">
        <form action="${pageContext.request.contextPath}/auth/cadastro" method="post">
            <div class="form-group">
                <label for="tipo">Tipo de Usuário</label>
                <select id="tipo" name="tipo" onchange="toggleCampos()" required>
                    <option value="">Selecione...</option>
                    <option value="cliente">Cliente</option>
                    <option value="prestador">Prestador</option>
                </select>
            </div>

            <div class="form-group">
                <label for="nome">Nome completo</label>
                <input type="text" id="nome" name="nome" required>
            </div>

            <div class="form-group">
                <label for="email">E-mail</label>
                <input type="email" id="email" name="email" required>
            </div>

            <div class="form-group">
                <label for="senha">Senha</label>
                <input type="password" id="senha" name="senha" required>
            </div>

            <!-- Campos específicos do CLIENTE -->
            <div id="campos-cliente" class="hidden">
                <div class="form-group">
                    <label for="cpf">CPF</label>
                    <input type="text" id="cpf" name="cpf" placeholder="000.000.000-00">
                </div>
            </div>

            <!-- Campos específicos do PRESTADOR -->
            <div id="campos-prestador" class="hidden">
                <div class="form-group">
                    <label for="telefone">Telefone</label>
                    <input type="text" id="telefone" name="telefone">
                </div>
                <div class="form-group">
                    <label for="especializacao">Especialização</label>
                    <input type="text" id="especializacao" name="especializacao">
                </div>
                <div class="form-group">
                    <label for="cnpj">CNPJ</label>
                    <input type="text" id="cnpj" name="cnpj">
                </div>
            </div>

            <div class="form-actions">
                <button class="btn btn-lg" type="submit">Cadastrar</button>
            </div>
        </form>
        
        <p style="text-align:center; margin-top:15px;">
            Já tem conta? <a href="login.jsp">Fazer Login</a><br>
            <a href="../index.jsp" style="font-size: 0.9em; color: #666;">← Voltar à página inicial</a>
        </p>
    </div>
</main>

<footer>
    &copy; 2025 AgendaSaaS.
</footer>
</body>
</html>
