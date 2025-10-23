<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:set var="pageTitle" value="AgendaSaaS - Agendamentos Online" />
<c:set var="pageRole" value="Home" />
<c:set var="pageCss" value="main{display:flex;flex-direction:column;align-items:center;justify-content:center;padding:40px 20px;background-color:var(--cor-fundo);} .cta{ text-align:center;margin-bottom:30px;color:var(--cor-texto);} .cta h2{margin-bottom:10px;font-size:1.8em;color:var(--cor-primaria);} .acoes{display:flex;gap:20px;justify-content:center;flex-wrap:wrap;} .btn-lg{font-size:1.1em;padding:14px 28px;} @media (max-width:600px){ .btn-lg{width:100%;text-align:center;} .acoes{flex-direction:column;align-items:stretch;} }" />

<jsp:include page="/WEB-INF/views/includes/header.jsp" />

    <main>
        <div class="cta">
            <h2>Seja bem-vindo!</h2>
            <p>Escolha abaixo como deseja continuar:</p>
        </div>

        <div class="acoes">
            <a class="btn btn-lg" href="auth/login.jsp">Fazer Login</a>
            <a class="btn btn-lg" href="auth/cadastro.jsp">Criar Conta</a>
        </div>
        
        <div style="margin-top: 30px; padding: 15px; background: #e8f5e8; border: 1px solid #4CAF50; border-radius: 6px; text-align: center;">
            <p style="margin: 0; color: #2e7d32;">
                ✓ <strong>Servidor funcionando!</strong><br>
                <small>AgendaSaaS está rodando em http://localhost:8080/agenda-saas/</small>
            </p>
        </div>
    </main>

<jsp:include page="/WEB-INF/views/includes/footer.jsp" />

