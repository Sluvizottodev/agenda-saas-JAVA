<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isErrorPage="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:set var="pageTitle" value="Erro - AgendaSaaS" />
<c:set var="pageRole" value="Erro" />
<c:set var="pageCss" value=".error-container{max-width:800px;margin:40px auto;background:#fff;padding:40px;border-radius:8px;box-shadow:var(--sombra-padrao);text-align:center;} .error-icon{font-size:4em;color:#e74c3c;margin-bottom:20px;} .error-title{color:#e74c3c;font-size:2em;margin-bottom:20px;font-weight:bold;} .error-details{background:#f8f9fa;border-left:4px solid #e74c3c;padding:15px;margin:20px 0;text-align:left;border-radius:0 4px 4px 0;}" />

<jsp:include page="/WEB-INF/views/includes/header.jsp" />

    <main>
        <div class="error-container">
            <div class="error-icon">⚠️</div>
            
            <h2 class="error-title">Oops! Algo deu errado</h2>
            
            <div class="error-message">
                <%
                    String errorMessage = (String) request.getAttribute("erro");
                    String errorType = (String) request.getAttribute("tipoErro");
                    Integer statusCode = (Integer) request.getAttribute("statusCode");
                    
                    if (errorMessage == null && exception != null) {
                        errorMessage = exception.getMessage();
                    }
                    
                    if (errorMessage == null) {
                        errorMessage = "Ocorreu um erro inesperado durante o processamento da sua solicitação.";
                    }
                    
                    if (statusCode == null) {
                        statusCode = response.getStatus();
                        if (statusCode == 200) statusCode = 500;
                    }
                %>
                
                <p><%= errorMessage %></p>
                
                <% if (statusCode != null && statusCode != 200) { %>
                    <p>Código do erro: <span class="error-code"><%= statusCode %></span></p>
                <% } %>
            </div>

            <% if (errorType != null || (exception != null && !"production".equals(application.getInitParameter("environment")))) { %>
                <div class="error-details">
                    <h4>Detalhes Técnicos:</h4>
                    <% if (errorType != null) { %>
                        <p><strong>Tipo:</strong> <%= errorType %></p>
                    <% } %>
                    <% if (exception != null && !"production".equals(application.getInitParameter("environment"))) { %>
                        <p><strong>Exceção:</strong> <%= exception.getClass().getSimpleName() %></p>
                        <% if (exception.getMessage() != null) { %>
                            <p><strong>Mensagem:</strong> <%= exception.getMessage() %></p>
                        <% } %>
                    <% } %>
                    <% String detalhes = (String) request.getAttribute("detalhes");
                       if (detalhes != null) { %>
                        <h5>Stack trace:</h5>
                        <pre style="max-height:320px;overflow:auto;background:#fff;padding:12px;border-radius:6px;border:1px solid #eee;"><%= detalhes %></pre>
                    <% } %>
                </div>
            <% } %>

            <div class="action-buttons">
                <a href="javascript:history.back()" class="btn btn-secondary">Voltar</a>
                <a href="${pageContext.request.contextPath}/" class="btn">Página Inicial</a>
                <% 
                    Object usuarioLogado = session.getAttribute("usuarioLogado");
                    if (usuarioLogado != null) {
                %>
                    <a href="${pageContext.request.contextPath}/dashboard" class="btn">Meu Dashboard</a>
                <% } else { %>
                    <a href="${pageContext.request.contextPath}/auth/login.jsp" class="btn">Fazer Login</a>
                <% } %>
            </div>
        </div>
    </main>

<jsp:include page="/WEB-INF/views/includes/footer.jsp" />

