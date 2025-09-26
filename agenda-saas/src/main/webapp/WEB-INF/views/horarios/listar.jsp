<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="br.cefet.agendaSaas.model.entidades.HorarioDisponivel" %>
<%@ page import="java.util.List" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Meus Horários - AgendaSaaS</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/style.css">
    <style>
        .horarios-container {
            max-width: 1000px;
            margin: 40px auto;
            background: #fff;
            padding: 30px;
            border-radius: 8px;
            box-shadow: var(--sombra-padrao);
        }

        .header-actions {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 20px;
        }

        .btn-group {
            display: flex;
            gap: 10px;
        }

        .table-container {
            overflow-x: auto;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }

        table th,
        table td {
            padding: 12px;
            text-align: left;
            border-bottom: 1px solid #ddd;
        }

        table th {
            background-color: var(--cor-primaria);
            color: white;
            font-weight: 600;
        }

        table tr:hover {
            background-color: #f5f5f5;
        }

        .status-badge {
            padding: 4px 12px;
            border-radius: 20px;
            font-size: 0.85em;
            font-weight: bold;
        }

        .status-disponivel {
            background-color: #d4edda;
            color: #155724;
        }

        .status-ocupado {
            background-color: #f8d7da;
            color: #721c24;
        }

        .actions {
            display: flex;
            gap: 8px;
        }

        .btn-sm {
            padding: 6px 12px;
            font-size: 0.85em;
            border-radius: 4px;
            text-decoration: none;
            border: none;
            cursor: pointer;
        }

        .btn-edit {
            background-color: #ffc107;
            color: #212529;
        }

        .btn-edit:hover {
            background-color: #e0a800;
        }

        .btn-delete {
            background-color: #dc3545;
            color: white;
        }

        .btn-delete:hover {
            background-color: #c82333;
        }

        .empty-state {
            text-align: center;
            padding: 40px;
            color: #666;
        }

        @media (max-width: 768px) {
            .horarios-container {
                margin: 20px;
                padding: 20px;
            }
            
            .header-actions {
                flex-direction: column;
                gap: 15px;
                align-items: stretch;
            }
            
            .btn-group {
                flex-direction: column;
            }
        }
    </style>
    <script>
        function confirmarRemocao(id, data, hora) {
            if (confirm('Tem certeza que deseja remover o horário do dia ' + data + ' às ' + hora + '?')) {
                window.location.href = '${pageContext.request.contextPath}/horarios/remover?id=' + id;
            }
        }
    </script>
</head>
<body>
    <%
        @SuppressWarnings("unchecked")
        List<HorarioDisponivel> horarios = (List<HorarioDisponivel>) request.getAttribute("horarios");
        String mensagem = (String) request.getAttribute("mensagem");
        String erro = (String) request.getAttribute("erro");
        
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
    %>

    <header>
        <h1>AgendaSaaS</h1>
        <p>Gerenciar Horários Disponíveis</p>
        <div style="text-align: right;">
            <a href="${pageContext.request.contextPath}/dashboard" class="btn btn-secondary">Voltar ao Dashboard</a>
        </div>
    </header>

    <main>
        <div class="horarios-container">
            <div class="header-actions">
                <h2>Meus Horários</h2>
                <div class="btn-group">
                    <a href="${pageContext.request.contextPath}/horarios/cadastrar" class="btn">
                        Novo Horário
                    </a>
                    <a href="${pageContext.request.contextPath}/horarios/gerar" class="btn btn-secondary">
                        Gerar Horários
                    </a>
                </div>
            </div>

            <% if (mensagem != null) { %>
                <div class="alert alert-success">
                    <%= mensagem %>
                </div>
            <% } %>

            <% if (erro != null) { %>
                <div class="alert alert-error">
                    <%= erro %>
                </div>
            <% } %>

            <% if (horarios != null && !horarios.isEmpty()) { %>
                <div class="table-container">
                    <table>
                        <thead>
                            <tr>
                                <th>Data</th>
                                <th>Hora Início</th>
                                <th>Hora Fim</th>
                                <th>Status</th>
                                <th>Ações</th>
                            </tr>
                        </thead>
                        <tbody>
                            <% for (HorarioDisponivel horario : horarios) { %>
                                <tr>
                                    <td><%= horario.getData().format(dateFormatter) %></td>
                                    <td><%= horario.getHoraInicio().format(timeFormatter) %></td>
                                    <td><%= horario.getHoraFim().format(timeFormatter) %></td>
                                    <td>
                                        <span class="status-badge <%= horario.isDisponivel() ? "status-disponivel" : "status-ocupado" %>">
                                            <%= horario.isDisponivel() ? "Disponível" : "Ocupado" %>
                                        </span>
                                    </td>
                                    <td>
                                        <div class="actions">
                                            <a href="${pageContext.request.contextPath}/horarios/editar?id=<%= horario.getId() %>" 
                                               class="btn-sm btn-edit">
                                                Editar
                                            </a>
                                            <button onclick="confirmarRemocao(<%= horario.getId() %>, '<%= horario.getData().format(dateFormatter) %>', '<%= horario.getHoraInicio().format(timeFormatter) %>')" 
                                                    class="btn-sm btn-delete">
                                                Remover
                                            </button>
                                        </div>
                                    </td>
                                </tr>
                            <% } %>
                        </tbody>
                    </table>
                </div>
            <% } else { %>
                <div class="empty-state">
                    <p>Você ainda não cadastrou nenhum horário disponível.</p>
                    <p>
                        <a href="${pageContext.request.contextPath}/horarios/cadastrar" class="btn">
                            Cadastrar Primeiro Horário
                        </a>
                    </p>
                </div>
            <% } %>
        </div>
    </main>

    <footer style="text-align: center; padding: 20px; color: #666;">
        &copy; 2025 AgendaSaaS - Sistema de Agendamento Online
    </footer>
</body>
</html>