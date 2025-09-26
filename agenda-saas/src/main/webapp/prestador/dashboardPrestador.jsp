<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>Dashboard - Prestador | AgendaSaaS</title>
    <link rel="stylesheet" href="../style.css">
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
    <h1>AgendaSaaS - Prestador</h1>
    <p>Bem-vindo, <strong>${prestador.nome}</strong></p>
</header>

<main>
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
            <a href="servicoCadastro.jsp" class="btn">Adicionar Novo Serviço</a>
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
</main>

<footer>
    &copy; 2025 AgendaSaaS
</footer>
</body>
</html>
