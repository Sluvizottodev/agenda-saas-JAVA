<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:set var="pageTitle" value="Login - AgendaSaaS" />
<c:set var="pageRole" value="Auth" />
<c:set var="pageCss" value=".form-container{max-width:420px;margin:40px auto;padding:20px;background:#fff;border-radius:8px;box-shadow:var(--sombra-padrao);} .form-actions{display:flex;justify-content:center;}" />

<jsp:include page="/WEB-INF/views/includes/header.jsp" />

<main>
    <div class="card form-container">
        <h3>Entrar na sua conta</h3>
        <p class="muted">Use seu e-mail e senha para entrar</p>

        <c:if test="${not empty erro}">
            <div class="alert alert-error">${erro}</div>
        </c:if>

        <form action="${pageContext.request.contextPath}/auth/login" method="post" class="form-login">
            <div class="form-group">
                <label for="email">E-mail</label>
                <input type="email" id="email" name="email" placeholder="seu@exemplo.com" required autofocus>
            </div>

            <div class="form-group">
                <label for="senha">Senha</label>
                <input type="password" id="senha" name="senha" placeholder="Sua senha" required>
            </div>

            <div class="form-row">
                <label class="checkbox-inline"><input type="checkbox" name="lembrar" value="1"> Lembrar-me</label>
                <a class="link-right" href="#">Esqueceu a senha?</a>
            </div>

            <div class="form-actions">
                <button class="btn btn-lg" type="submit">Entrar</button>
            </div>
        </form>


        <p class="muted" style="text-align:center; margin-top:12px;">
            Ainda não tem conta? <a href="cadastro.jsp">Cadastre-se</a>
        </p>
        <p style="text-align:center; margin-top:6px;"><a href="${pageContext.request.contextPath}/" class="muted">← Voltar à página inicial</a></p>
    </div>
</main>

<jsp:include page="/WEB-INF/views/includes/footer.jsp" />

