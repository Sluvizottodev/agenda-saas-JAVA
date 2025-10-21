<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="br.cefet.agendasaas.model.entidades.Usuario" %>
<%@ page import="br.cefet.agendasaas.model.entidades.Servico" %>
<%@ page import="java.util.List" %>

<%-- Definições de cabeçalho e CSS por página --%>
<c:set var="pageTitle" value="Agendar - AgendaSaaS" />
<c:set var="pageRole" value="Cliente" />
<c:set var="pageCss" value="" />
<jsp:include page="/WEB-INF/views/includes/header.jsp" />

<script>
    function selecionarServico(elemento) {
        document.querySelectorAll('.servico-card').forEach(card => {
            card.classList.remove('selected');
        });

        elemento.classList.add('selected');
        const servicoId = elemento.getAttribute('data-servico-id') || elemento.dataset.servicoId;
        if (servicoId) {
            const input = document.getElementById('servico_' + servicoId);
            if (input) input.checked = true;
        }
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

        const dataHora = new Date(data + 'T' + hora);
        const agora = new Date();

        if (dataHora <= agora) {
            alert('A data e horário devem ser futuros.');
            return false;
        }

        return true;
    }

    window.addEventListener('DOMContentLoaded', function() {
        const hoje = new Date().toISOString().split('T')[0];
        const dataEl = document.getElementById('data');
        if (dataEl) dataEl.min = hoje;
    });
</script>

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
                            <div class="servico-card" data-servico-id="<%= servico.getId() %>" onclick="selecionarServico(this)">
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

    <jsp:include page="/WEB-INF/views/includes/footer.jsp" />
