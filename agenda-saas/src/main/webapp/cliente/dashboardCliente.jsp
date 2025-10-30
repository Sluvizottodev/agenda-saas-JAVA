<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="pageTitle" value="Dashboard - Cliente | AgendaSaaS" />
<c:set var="pageRole" value="Cliente" />
<c:set var="pageCss" value=".cards{gap:16px;} table th, table td{font-size:0.95rem;}" />
<jsp:include page="/WEB-INF/views/includes/header.jsp" />

    <h2>Seus Agendamentos</h2>
    <div class="cards">
        <div class="card">
            <table>
                <thead>
                    <tr>
                        <th>Prestador</th>
                        <th>Serviço</th>
                        <th>Data</th>
                        <th>Status</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="agendamento" items="${agendamentos}">
                        <tr>
                            <td>${agendamento.prestador.nome}</td>
                            <td>${agendamento.servico.nome}</td>
                            <td>${agendamento.data}</td>
                            <td>${agendamento.status}</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>

        <div class="card">
            <h3>Serviços Disponíveis</h3>
            <table>
                <thead>
                    <tr>
                        <th>Prestador</th>
                        <th>Serviço</th>
                        <th>Preço</th>
                        <th>Ação</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="servico" items="${servicos}">
                        <tr>
                            <td>${servico.prestador.nome}</td>
                            <td>${servico.nome}</td>
                            <td>R$ ${servico.preco}</td>
                            <td><a href="agendamento.jsp?servicoId=${servico.id}" class="btn">Agendar</a></td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>

<jsp:include page="/WEB-INF/views/includes/footer.jsp" />

