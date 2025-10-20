<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="pageTitle" value="Dashboard - Prestador | AgendaSaaS" />
<c:set var="pageRole" value="Prestador" />
<jsp:include page="/WEB-INF/views/includes/header.jsp" />

    <h2>Seus ServiÃ§os</h2>
    <div class="cards">
        <div class="card">
            <table>
                <thead>
                    <tr>
                        <th>Nome</th>
                        <th>DescriÃ§Ã£o</th>
                        <th>PreÃ§o</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="servico" items="${servicos}">
                        <tr>
                            <td>${servico.nome}</td>
                            <td>${servico.descricao}</td>
                            <td>R$ ${servico.preco}</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
            <a href="servicoCadastro.jsp" class="btn">Adicionar Novo ServiÃ§o</a>
        </div>

        <div class="card">
            <h3>Agendamentos</h3>
            <table>
                <thead>
                    <tr>
                        <th>Cliente</th>
                        <th>Data</th>
                        <th>Status</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="agendamento" items="${agendamentos}">
                        <tr>
                            <td>${agendamento.cliente.nome}</td>
                            <td>${agendamento.data}</td>
                            <td>${agendamento.status}</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>

<jsp:include page="/WEB-INF/views/includes/footer.jsp" />

