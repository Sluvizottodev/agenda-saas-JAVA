<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>Login - AgendaSaaS</title>
    <link rel="stylesheet" href="../style.css">
</head>
<body>
<header>
    <h1>AgendaSaaS</h1>
    <p>Entre para acessar sua conta</p>
</header>

<main>
    <div class="form-container">
        <form action="${pageContext.request.contextPath}/auth/login" method="post">
            <div class="form-group">
                <label for="email">E-mail</label>
                <input type="email" id="email" name="email" required>
            </div>

            <div class="form-group">
                <label for="senha">Senha</label>
                <input type="password" id="senha" name="senha" required>
            </div>

            <div class="form-actions">
                <button class="btn btn-lg" type="submit">Entrar</button>
            </div>
        </form>
        <p style="text-align:center; margin-top:10px;">
            Ainda nÃ£o tem conta? <a href="cadastro.jsp">Cadastre-se</a><br>
            <a href="../index.jsp" style="font-size: 0.9em; color: #666;">â† Voltar Ã  pÃ¡gina inicial</a>
        </p>
    </div>
</main>

<footer>
    &copy; 2025 AgendaSaaS.
</footer>
</body>
</html>

