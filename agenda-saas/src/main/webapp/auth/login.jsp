<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:set var="pageTitle" value="Login - AgendaSaaS" />
<c:set var="pageRole" value="Auth" />
<c:set var="pageCss" value=".form-container{max-width:420px;margin:40px auto;padding:20px;background:#fff;border-radius:8px;box-shadow:var(--sombra-padrao);} .form-actions{display:flex;justify-content:center;}" />

<jsp:include page="/WEB-INF/views/includes/header.jsp" />

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
            Ainda não tem conta? <a href="cadastro.jsp">Cadastre-se</a><br>
            <a href="${pageContext.request.contextPath}/" style="font-size: 0.9em; color: #666;">← Voltar à página inicial</a>
        </p>
    </div>
</main>

<jsp:include page="/WEB-INF/views/includes/footer.jsp" />

