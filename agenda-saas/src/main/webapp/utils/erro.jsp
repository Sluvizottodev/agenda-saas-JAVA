<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isErrorPage="true" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Erro - AgendaSaaS</title>
    <link rel="stylesheet" href="../style.css">
    <style>
        .error-container {
            max-width: 800px;
            margin: 40px auto;
            background: #fff;
            padding: 40px;
            border-radius: 8px;
            box-shadow: var(--sombra-padrao);
            text-align: center;
        }

        .error-icon {
            font-size: 4em;
            color: #e74c3c;
            margin-bottom: 20px;
        }

        .error-title {
            color: #e74c3c;
            font-size: 2em;
            margin-bottom: 20px;
            font-weight: bold;
        }

        .error-message {
            font-size: 1.2em;
            color: #555;
            margin-bottom: 30px;
            line-height: 1.5;
        }

        .error-details {
            background: #f8f9fa;
            border-left: 4px solid #e74c3c;
            padding: 15px;
            margin: 20px 0;
            text-align: left;
            border-radius: 0 4px 4px 0;
        }

        .error-details h4 {
            margin-top: 0;
            color: #e74c3c;
        }

        .error-code {
            font-family: 'Courier New', monospace;
            background: #f1f1f1;
            padding: 2px 6px;
            border-radius: 3px;
            color: #333;
        }

        .action-buttons {
            margin-top: 30px;
        }

        .action-buttons .btn {
            margin: 0 10px;
            padding: 12px 24px;
            font-size: 1.1em;
        }

        .btn-secondary {
            background-color: #6c757d;
            color: white;
        }

        .btn-secondary:hover {
            background-color: #5a6268;
        }

        @media (max-width: 600px) {
            .error-container {
                margin: 20px;
                padding: 20px;
            }
            
            .error-title {
                font-size: 1.5em;
            }
            
            .error-message {
                font-size: 1em;
            }
            
            .action-buttons .btn {
                display: block;
                width: 100%;
                margin: 10px 0;
            }
        }
    </style>
</head>
<body>
    <header>
        <h1>AgendaSaaS</h1>
        <p>Sistema de Agendamento Online</p>
    </header>

    <main>
        <div class="error-container">
            <div class="error-icon">âš ï¸</div>
            
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
                        errorMessage = "Ocorreu um erro inesperado durante o processamento da sua solicitaÃ§Ã£o.";
                    }
                    
                    if (statusCode == null) {
                        statusCode = response.getStatus();
                        if (statusCode == 200) statusCode = 500;
                    }
                %>
                
                <p><%= errorMessage %></p>
                
                <% if (statusCode != null && statusCode != 200) { %>
                    <p>CÃ³digo do erro: <span class="error-code"><%= statusCode %></span></p>
                <% } %>
            </div>

            <% if (errorType != null || (exception != null && !"production".equals(application.getInitParameter("environment")))) { %>
                <div class="error-details">
                    <h4>Detalhes TÃ©cnicos:</h4>
                    <% if (errorType != null) { %>
                        <p><strong>Tipo:</strong> <%= errorType %></p>
                    <% } %>
                    <% if (exception != null && !"production".equals(application.getInitParameter("environment"))) { %>
                        <p><strong>ExceÃ§Ã£o:</strong> <%= exception.getClass().getSimpleName() %></p>
                        <% if (exception.getMessage() != null) { %>
                            <p><strong>Mensagem:</strong> <%= exception.getMessage() %></p>
                        <% } %>
                    <% } %>
                </div>
            <% } %>

            <div class="action-buttons">
                <a href="javascript:history.back()" class="btn btn-secondary">Voltar</a>
                <a href="${pageContext.request.contextPath}/" class="btn">PÃ¡gina Inicial</a>
                <% 
                    // Verifica se hÃ¡ um usuÃ¡rio logado para mostrar o dashboard apropriado
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

    <footer style="text-align: center; padding: 20px; color: #666; margin-top: 40px;">
        <p>&copy; 2025 AgendaSaaS - Sistema de Agendamento Online</p>
        <p>Se o problema persistir, entre em contato com o suporte tÃ©cnico.</p>
    </footer>
</body>
</html>

