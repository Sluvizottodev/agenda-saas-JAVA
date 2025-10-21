<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="br.cefet.agendasaas.model.entidades.Agendamento" %>
<c:set var="pageTitle" value="Agendamento - AgendaSaaS" />
<c:set var="pageRole" value="Agendamento" />
<c:set var="pageCss" value=".sucesso-container{max-width:700px;margin:40px auto;} .sucesso-titulo{color:var(--cor-primaria);} " />
<jsp:include page="/WEB-INF/views/includes/header.jsp" />

<%
    String mensagem = (String) request.getAttribute("mensagem");
    Agendamento agendamento = (Agendamento) request.getAttribute("agendamento");
    if (mensagem == null) mensagem = "Agendamento realizado com sucesso!";
%>

    <div class="sucesso-container">
        <div class="sucesso-icon">✓</div>

        <h2 class="sucesso-titulo">Agendamento Confirmado!</h2>

        <div class="sucesso-mensagem">
            <%= mensagem %>
        </div>

        <% if (agendamento != null) { %>
            <div class="agendamento-detalhes">
                <h3>Detalhes do Agendamento</h3>
                <div class="detalhe-item">
                    <span class="detalhe-label">Número do Agendamento:</span>
                    <span class="detalhe-valor">#<%= agendamento.getId() %></span>
                </div>
                <div class="detalhe-item">
                    <span class="detalhe-label">Serviço:</span>
                    <span class="detalhe-valor">Serviço #<%= agendamento.getServicoId() %></span>
                </div>
                <div class="detalhe-item">
                    <span class="detalhe-label">Prestador:</span>
                    <span class="detalhe-valor">Prestador #<%= agendamento.getPrestadorId() %></span>
                </div>
                <div class="detalhe-item">
                    <span class="detalhe-label">Data e Horário:</span>
                    <span class="detalhe-valor"><%= agendamento.getDataHora() != null ? agendamento.getDataHora().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm")) : "N/A" %></span>
                </div>
                <div class="detalhe-item">
                    <span class="detalhe-label">Status:</span>
                    <span class="detalhe-valor"><span class="status-badge"><%= agendamento.getStatus() %></span></span>
                </div>
            </div>
        <% } %>

        <div class="info-adicional">
            <p><strong>Próximos passos:</strong></p>
            <ul style="text-align:left; margin:10px 0;">
                <li>O prestador será notificado sobre seu agendamento</li>
                <li>Você receberá uma confirmação por e-mail quando o agendamento for aprovado</li>
                <li>Você pode acompanhar o status do agendamento no seu dashboard</li>
            </ul>
        </div>

        <div class="acoes">
            <a href="${pageContext.request.contextPath}/agendamentos" class="btn btn-secondary">Ver Meus Agendamentos</a>
            <a href="${pageContext.request.contextPath}/agendar" class="btn">Fazer Novo Agendamento</a>
            <a href="${pageContext.request.contextPath}/dashboard" class="btn btn-cancelar">Voltar ao Dashboard</a>
        </div>
    </div>

<jsp:include page="/WEB-INF/views/includes/footer.jsp" />
