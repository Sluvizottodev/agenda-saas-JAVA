<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="br.cefet.agendasaas.model.entidades.Agendamento" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Agendamento Realizado - AgendaSaaS</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/style.css">
    <style>
        .sucesso-container {
            max-width: 600px;
            margin: 40px auto;
            background: #fff;
            padding: 40px;
            border-radius: 8px;
            box-shadow: var(--sombra-padrao);
            text-align: center;
        }

        .sucesso-icon {
            font-size: 4em;
            color: #28a745;
            margin-bottom: 20px;
        }

        .sucesso-titulo {
            color: #28a745;
            font-size: 2em;
            margin-bottom: 20px;
            font-weight: bold;
        }

        .sucesso-mensagem {
            font-size: 1.1em;
            color: #555;
            margin-bottom: 30px;
            line-height: 1.6;
        }

        .agendamento-detalhes {
            background: #f8f9fa;
            border: 1px solid #dee2e6;
            border-radius: 8px;
            padding: 20px;
            margin: 20px 0;
            text-align: left;
        }

        .agendamento-detalhes h3 {
            color: var(--cor-primaria);
            margin-bottom: 15px;
            text-align: center;
        }

        .detalhe-item {
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 10px 0;
            border-bottom: 1px solid #e9ecef;
        }

        .detalhe-item:last-child {
            border-bottom: none;
        }

        .detalhe-label {
            font-weight: bold;
            color: #666;
        }

        .detalhe-valor {
            color: #333;
        }

        .status-badge {
            padding: 6px 12px;
            border-radius: 20px;
            font-size: 0.9em;
            font-weight: bold;
            text-transform: uppercase;
            background-color: #fff3cd;
            color: #856404;
            border: 1px solid #ffeaa7;
        }

        .acoes {
            margin-top: 30px;
        }

        .acoes .btn {
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

        .info-adicional {
            background: #d1ecf1;
            border: 1px solid #bee5eb;
            color: #0c5460;
            padding: 15px;
            border-radius: 6px;
            margin-top: 20px;
        }

        @media (max-width: 600px) {
            .sucesso-container {
                margin: 20px;
                padding: 20px;
            }
            
            .sucesso-titulo {
                font-size: 1.5em;
            }
            
            .acoes .btn {
                display: block;
                width: 100%;
                margin: 10px 0;
            }
            
            .detalhe-item {
                flex-direction: column;
                align-items: flex-start;
            }
            
            .detalhe-valor {
                margin-top: 5px;
            }
        }
    </style>
</head>
<body>
    <%
        String mensagem = (String) request.getAttribute("mensagem");
        Agendamento agendamento = (Agendamento) request.getAttribute("agendamento");
        
        if (mensagem == null) {
            mensagem = "Agendamento realizado com sucesso!";
        }
    %>

    <header>
        <h1>AgendaSaaS</h1>
        <p>Sistema de Agendamento Online</p>
    </header>

    <main>
        <div class="sucesso-container">
            <div class="sucesso-icon">âœ…</div>
            
            <h2 class="sucesso-titulo">Agendamento Confirmado!</h2>
            
            <div class="sucesso-mensagem">
                <%= mensagem %>
            </div>

            <% if (agendamento != null) { %>
                <div class="agendamento-detalhes">
                    <h3>Detalhes do Agendamento</h3>
                    
                    <div class="detalhe-item">
                        <span class="detalhe-label">NÃºmero do Agendamento:</span>
                        <span class="detalhe-valor">#<%= agendamento.getId() %></span>
                    </div>
                    
                    <div class="detalhe-item">
                        <span class="detalhe-label">ServiÃ§o:</span>
                        <span class="detalhe-valor">ServiÃ§o #<%= agendamento.getServicoId() %></span>
                    </div>
                    
                    <div class="detalhe-item">
                        <span class="detalhe-label">Prestador:</span>
                        <span class="detalhe-valor">Prestador #<%= agendamento.getPrestadorId() %></span>
                    </div>
                    
                    <div class="detalhe-item">
                        <span class="detalhe-label">Data e HorÃ¡rio:</span>
                        <span class="detalhe-valor">
                            <%= agendamento.getDataHora() != null ? 
                                agendamento.getDataHora().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy 'Ã s' HH:mm")) : 
                                "N/A" %>
                        </span>
                    </div>
                    
                    <div class="detalhe-item">
                        <span class="detalhe-label">Status:</span>
                        <span class="detalhe-valor">
                            <span class="status-badge"><%= agendamento.getStatus() %></span>
                        </span>
                    </div>
                </div>
            <% } %>

            <div class="info-adicional">
                <p><strong>PrÃ³ximos passos:</strong></p>
                <ul style="text-align: left; margin: 10px 0;">
                    <li>O prestador serÃ¡ notificado sobre seu agendamento</li>
                    <li>VocÃª receberÃ¡ uma confirmaÃ§Ã£o por email quando o agendamento for aprovado</li>
                    <li>VocÃª pode acompanhar o status do agendamento no seu dashboard</li>
                </ul>
            </div>

            <div class="acoes">
                <a href="${pageContext.request.contextPath}/agendamentos" class="btn btn-secondary">
                    Ver Meus Agendamentos
                </a>
                <a href="${pageContext.request.contextPath}/agendar" class="btn">
                    Fazer Novo Agendamento
                </a>
                <a href="${pageContext.request.contextPath}/dashboard" class="btn">
                    Voltar ao Dashboard
                </a>
            </div>
        </div>
    </main>

    <footer style="text-align: center; padding: 20px; color: #666;">
        &copy; 2025 AgendaSaaS - Sistema de Agendamento Online
    </footer>
</body>
</html>
