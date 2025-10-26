<%@ page pageEncoding="UTF-8" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:set var="pageTitle" value="AgendaSaaS - Agendamentos Online" />
<c:set var="pageRole" value="Home" />
<c:set var="pageCss" value=".hero{display:flex;flex-direction:column;align-items:center;justify-content:center;padding:48px 20px;background-color:var(--cor-fundo);border-radius:8px;margin-top:18px;} .hero .cta{ text-align:center;margin-bottom:16px;color:var(--cor-texto);} .hero h2{margin-bottom:8px;font-size:2rem;color:var(--cor-primaria);} .acoes{display:flex;gap:18px;justify-content:center;flex-wrap:wrap;margin-top:10px;} .btn-lg{font-size:1.05em;padding:12px 26px;border-radius:10px;} @media (max-width:600px){ .btn-lg{width:100%;text-align:center;} .acoes{flex-direction:column;align-items:stretch;} }" />

<jsp:include page="/WEB-INF/views/includes/header.jsp" />
        <section class="hero">
            <div class="cta">
                <h2>Seja bem-vindo ao AgendaSaaS</h2>
                <p>Agende serviços com facilidade — rápido, seguro e organizado.</p>
            </div>

            <div class="acoes">
                <a class="btn btn-lg" href="auth/login.jsp">Entrar</a>
                <a class="btn btn-lg" href="auth/cadastro.jsp">Criar Conta</a>
            </div>

            <div class="card" style="margin-top:20px; max-width:720px;">
                <p style="margin:0; color:#2e7d32;">✓ <strong>Servidor funcionando!</strong>
                <br><small>Abra <a href="${pageContext.request.contextPath}/">a página inicial</a> ou acesse diretamente <code>/agenda-saas/</code></small></p>
            </div>
    </section>

    <section style="margin-top:28px;">
            <div class="card">
                <h3>Recursos</h3>
                <ul style="text-align:left; margin:12px 0 0 18px;">
                    <li>Cadastro de clientes e prestadores</li>
                    <li>Agendamento com horários disponíveis</li>
                    <li>Dashboard para acompanhar seus agendamentos</li>
                </ul>
            </div>
        </section>

<jsp:include page="/WEB-INF/views/includes/footer.jsp" />

