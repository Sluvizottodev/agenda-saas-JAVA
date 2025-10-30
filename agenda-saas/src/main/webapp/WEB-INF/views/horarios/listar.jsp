<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="br.cefet.agendasaas.model.entidades.HorarioDisponivel" %>
<%@ page import="java.util.List" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:set var="pageTitle" value="Meus Horários - AgendaSaaS" />
<c:set var="pageRole" value="Prestador" />
<c:set var="pageCss" value=".horarios-container{max-width:1000px;margin:40px auto;background:#fff;padding:30px;border-radius:8px;box-shadow:var(--sombra-padrao);} .header-actions{display:flex;justify-content:space-between;align-items:center;margin-bottom:20px;} .table-container{overflow-x:auto;}" />

<jsp:include page="/WEB-INF/views/includes/header.jsp" />

<script>
    function confirmarRemocao(buttonEl) {
        const id = buttonEl.dataset.id;
        const data = buttonEl.dataset.data;
        const hora = buttonEl.dataset.hora;
        if (confirm('Tem certeza que deseja remover o horário do dia ' + data + ' - ' + hora + '?')) {
            window.location.href = '${pageContext.request.contextPath}/horarios/remover?id=' + id;
        }
    }
</script>

<%
    @SuppressWarnings("unchecked")
    List<HorarioDisponivel> horarios = (List<HorarioDisponivel>) request.getAttribute("horarios");
    String mensagem = (String) request.getAttribute("mensagem");
    String erro = (String) request.getAttribute("erro");
    
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
%>

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
                                        <button data-id="<%= horario.getId() %>" data-data="<%= horario.getData().format(dateFormatter) %>" data-hora="<%= horario.getHoraInicio().format(timeFormatter) %>" onclick="confirmarRemocao(this)" 
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

<jsp:include page="/WEB-INF/views/includes/footer.jsp" />
