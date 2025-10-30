<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title><c:out value="${pageTitle}" default="AgendaSaaS"/></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/style.css">
    <c:if test="${not empty pageCss}">
        <style>
            <c:out value="${pageCss}" escapeXml="false"/>
        </style>
    </c:if>
</head>
<body>

<header>
    <div class="container header-bar">
        <div>
            <h1>AgendaSaaS</h1>
            <div class="header-subtitle"> <c:out value="${pageRole}" default="Área"/></div>
        </div>

        <nav>
            <a href="${pageContext.request.contextPath}/" class="btn">Início</a>
            <c:choose>
                    <c:when test="${not empty sessionScope.usuarioLogado}">
                        <c:choose>
                            <c:when test="${sessionScope.usuarioLogado.tipo == 'CLIENTE'}">
                                <a href="${pageContext.request.contextPath}/dashboard/cliente" class="btn btn-secondary">Dashboard</a>
                            </c:when>
                            <c:when test="${sessionScope.usuarioLogado.tipo == 'PRESTADOR'}">
                                <a href="${pageContext.request.contextPath}/dashboard/prestador" class="btn btn-secondary">Dashboard</a>
                            </c:when>
                            <c:otherwise>
                                <a href="${pageContext.request.contextPath}/" class="btn btn-secondary">Dashboard</a>
                            </c:otherwise>
                        </c:choose>
                        <a href="${pageContext.request.contextPath}/auth/logout" class="btn btn-cancelar">Sair</a>
                    </c:when>
                <c:otherwise>
                    <a href="${pageContext.request.contextPath}/auth/login.jsp" class="btn btn-secondary">Entrar</a>
                    <a href="${pageContext.request.contextPath}/auth/cadastro.jsp" class="btn">Cadastrar</a>
                </c:otherwise>
            </c:choose>
        </nav>
    </div>
</header>

<main class="container">

