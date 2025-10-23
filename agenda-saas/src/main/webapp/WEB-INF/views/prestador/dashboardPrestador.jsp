<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="pageTitle" value="Dashboard - Prestador | AgendaSaaS" />
<c:set var="pageRole" value="Prestador" />
<jsp:include page="/WEB-INF/views/includes/header.jsp" />

    <h2>Seus Serviços</h2>
    <div class="cards">
        <div class="card">
            <table>
                <thead>
                    <tr>
                        <th>Nome</th>
                        <th>Descrição</th>
                        <th>Preço</th>
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
            <a href="${pageContext.request.contextPath}/servico/cadastrar" class="btn">Adicionar Novo Serviço</a>
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

