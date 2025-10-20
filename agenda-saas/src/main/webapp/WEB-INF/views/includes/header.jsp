<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title><c:out value="${pageTitle}" default="AgendaSaaS"/></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/style.css">
    <style>
        main {
            max-width: 1200px;
            margin: 40px auto;
            padding: 20px;
            display: flex;
            flex-direction: column;
            gap: 30px;
        }

        h2 {
            color: var(--cor-primaria);
        }

        .cards {
            display: flex;
            flex-wrap: wrap;
            gap: 20px;
        }

        .card {
            flex: 1 1 300px;
            background: #fff;
            padding: 20px;
            border-radius: 8px;
            box-shadow: var(--sombra-padrao);
        }

        table {
            width: 100%;
            border-collapse: collapse;
        }

        table th, table td {
            padding: 10px;
            border-bottom: 1px solid #ddd;
            text-align: left;
        }

        .btn {
            display: inline-block;
            padding: 10px 18px;
            background-color: var(--cor-primaria);
            color: #fff;
            text-decoration: none;
            border-radius: 6px;
        }

        @media (max-width: 768px) {
            .cards {
                flex-direction: column;
            }
        }
    </style>
</head>
<body>
<header>
    <h1>AgendaSaaS - <c:out value="${pageRole}" default="Ãrea"/></h1>
    <p>Bem-vindo, <strong>
        <c:choose>
            <c:when test="${not empty cliente}">${cliente.nome}</c:when>
            <c:when test="${not empty prestador}">${prestador.nome}</c:when>
            <c:otherwise><c:out value="${userName}" default="UsuÃ¡rio"/></c:otherwise>
        </c:choose>
    </strong></p>
</header>

<main>

