<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="br.cefet.agendaSaas.model.entidades.Usuario" %>
<%@ page import="br.cefet.agendaSaas.model.entidades.Servico" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Novo Agendamento - AgendaSaaS</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/style.css">
    <style>
        .agendamento-container {
            max-width: 800px;
            margin: 40px auto;
            background: #fff;
            padding: 30px;
            border-radius: 8px;
            box-shadow: var(--sombra-padrao);
        }

        .form-group {
            margin-bottom: 20px;
        }

        .form-group label {
            display: block;
            font-weight: 600;
            margin-bottom: 8px;
            color: var(--cor-primaria);
        }

        .form-group input, 
        .form-group select, 
        .form-group textarea {
            width: 100%;
            padding: 12px;
            border: 2px solid #e1e5e9;
            border-radius: 6px;
            font-size: 1em;
            transition: var(--transicao);
        }

        .form-group input:focus, 
        .form-group select:focus, 
        .form-group textarea:focus {
            border-color: var(--cor-secundaria);
            outline: none;
            box-shadow: 0 0 0 3px rgba(123, 97, 255, 0.1);
        }

        .form-row {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 20px;
        }

        .servico-card {
            border: 2px solid #e1e5e9;
            border-radius: 8px;
            padding: 15px;
            margin-bottom: 10px;
            cursor: pointer;
            transition: var(--transicao);
        }

        .servico-card:hover {
            border-color: var(--cor-secundaria);
            transform: translateY(-2px);
        }

        .servico-card.selected {
            border-color: var(--cor-secundaria);
            background-color: rgba(123, 97, 255, 0.1);
        }

        .servico-card input[type="radio"] {
            margin-right: 10px;
        }

        .servico-info {
            margin-left: 25px;
        }

        .servico-nome {
            font-weight: bold;
            color: var(--cor-primaria);
            margin-bottom: 5px;
        }

        .servico-descricao {
            color: #666;
            font-size: 0.9em;
            margin-bottom: 8px;
        }

        .servico-preco {
            color: var(--cor-secundaria);
            font-weight: bold;
            font-size: 1.1em;
        }

        .btn-group {
            display: flex;
            gap: 15px;
            justify-content: center;
            margin-top: 30px;
        }

        .btn-cancelar {
            background-color: #6c757d;
            color: white;
        }

        .btn-cancelar:hover {
            background-color: #5a6268;
        }

        .alert {
            padding: 15px;
            border-radius: 6px;
            margin-bottom: 20px;
        }

        .alert-success {
            background-color: #d4edda;
            border: 1px solid #c3e6cb;
            color: #155724;
        }

        .alert-error {
            background-color: #f8d7da;
            border: 1px solid #f5c6cb;
            color: #721c24;
        }

        .alert-info {
            background-color: #d1ecf1;
            border: 1px solid #bee5eb;
            color: #0c5460;
        }

        @media (max-width: 768px) {
            .agendamento-container {
                margin: 20px;
                padding: 20px;
            }

            .form-row {
                grid-template-columns: 1fr;
            }

            .btn-group {
                flex-direction: column;
            }
        }
    </style>
    <script>
        function selecionarServico(servicoId, elemento) {
            // Remove seleção anterior
            document.querySelectorAll('.servico-card').forEach(card => {
                card.classList.remove('selected');
            });
            
            // Adiciona seleção atual
            elemento.classList.add('selected');
            
            // Marca o radio button
            document.getElementById('servico_' + servicoId).checked = true;
        }

        function validarFormulario() {
            const servicoSelecionado = document.querySelector('input[name="servicoId"]:checked');
            const data = document.getElementById('data').value;
            const hora = document.getElementById('hora').value;

            if (!servicoSelecionado) {
                alert('Por favor, selecione um serviço.');
                return false;
            }

            if (!data) {
                alert('Por favor, selecione uma data.');
                return false;
            }

            if (!hora) {
                alert('Por favor, selecione um horário.');
                return false;
            }

            // Validar se a data não é no passado
            const dataHora = new Date(data + 'T' + hora);
            const agora = new Date();

            if (dataHora <= agora) {
                alert('A data e horário devem ser futuros.');
                return false;
            }

            return true;
        }

        // Definir data mínima como hoje
        window.addEventListener('DOMContentLoaded', function() {
            const hoje = new Date().toISOString().split('T')[0];
            document.getElementById('data').min = hoje;
        });
    </script>
</head>
<body>
    <%
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
        List<Servico> servicosDisponiveis = (List<Servico>) request.getAttribute("servicosDisponiveis");
        String mensagem = (String) request.getAttribute("mensagem");
        String erro = (String) request.getAttribute("erro");

        if (usuario == null) {
            response.sendRedirect(request.getContextPath() + "/auth/login.jsp");
            return;
        }
    %>

    <header>
        <h1>AgendaSaaS</h1>
        <p>Novo Agendamento</p>
        <div style="text-align: right;">
            <a href="${pageContext.request.contextPath}/dashboard" class="btn btn-secondary">Voltar ao Dashboard</a>
        </div>
    </header>

    <main>
        <div class="agendamento-container">
            <h2>Agendar Novo Serviço</h2>

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

            <form action="${pageContext.request.contextPath}/agendar" method="post" onsubmit="return validarFormulario()">
                <div class="form-group">
                    <label>Selecione o Serviço:</label>
                    <% if (servicosDisponiveis != null && !servicosDisponiveis.isEmpty()) { %>
                        <% for (Servico servico : servicosDisponiveis) { %>
                            <div class="servico-card" onclick="selecionarServico(<%= servico.getId() %>, this)">
                                <input type="radio" id="servico_<%= servico.getId() %>" name="servicoId" value="<%= servico.getId() %>" style="display: none;">
                                <label for="servico_<%= servico.getId() %>" style="cursor: pointer; display: block;">
                                    <div class="servico-nome"><%= servico.getNome() %></div>
                                    <div class="servico-info">
                                        <div class="servico-descricao"><%= servico.getDescricao() %></div>
                                        <div class="servico-preco">R$ <%= String.format("%.2f", servico.getPreco()) %></div>
                                    </div>
                                </label>
                            </div>
                        <% } %>
                    <% } else { %>
                        <div class="alert alert-info">
                            <p>Nenhum serviço disponível no momento. Entre em contato conosco para mais informações.</p>
                        </div>
                    <% } %>
                </div>

                <% if (servicosDisponiveis != null && !servicosDisponiveis.isEmpty()) { %>
                    <div class="form-row">
                        <div class="form-group">
                            <label for="data">Data:</label>
                            <input type="date" id="data" name="data" required>
                        </div>

                        <div class="form-group">
                            <label for="hora">Horário:</label>
                            <select id="hora" name="hora" required>
                                <option value="">Selecione um horário</option>
                                <option value="08:00">08:00</option>
                                <option value="08:30">08:30</option>
                                <option value="09:00">09:00</option>
                                <option value="09:30">09:30</option>
                                <option value="10:00">10:00</option>
                                <option value="10:30">10:30</option>
                                <option value="11:00">11:00</option>
                                <option value="11:30">11:30</option>
                                <option value="13:00">13:00</option>
                                <option value="13:30">13:30</option>
                                <option value="14:00">14:00</option>
                                <option value="14:30">14:30</option>
                                <option value="15:00">15:00</option>
                                <option value="15:30">15:30</option>
                                <option value="16:00">16:00</option>
                                <option value="16:30">16:30</option>
                                <option value="17:00">17:00</option>
                                <option value="17:30">17:30</option>
                            </select>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="observacoes">Observações (opcional):</label>
                        <textarea id="observacoes" name="observacoes" rows="4" placeholder="Descreva alguma observação especial sobre o agendamento..."></textarea>
                    </div>

                    <div class="btn-group">
                        <a href="${pageContext.request.contextPath}/dashboard" class="btn btn-cancelar">Cancelar</a>
                        <button type="submit" class="btn">Confirmar Agendamento</button>
                    </div>
                <% } %>
            </form>
        </div>
    </main>

    <footer style="text-align: center; padding: 20px; color: #666;">
        &copy; 2025 AgendaSaaS - Sistema de Agendamento Online
    </footer>
</body>
</html>
