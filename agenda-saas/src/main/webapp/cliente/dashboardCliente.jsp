<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="pageTitle" value="Dashboard - Cliente | AgendaSaaS" />
<c:set var="pageRole" value="Cliente" />
<jsp:include page="/WEB-INF/views/includes/header.jsp" />

    <h2>Seus Agendamentos</h2>
    <div class="cards">
        <div class="card">
            <table>
                <thead>
                    <tr>
                        <th>Prestador</th>
                        <th>ServiÃ§o</th>
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
            <h3>ServiÃ§os DisponÃ­veis</h3>
            <table>
                <thead>
                    <tr>
                        <th>Prestador</th>
                        <th>ServiÃ§o</th>
                        <th>PreÃ§o</th>
                        <th>AÃ§Ã£o</th>
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

