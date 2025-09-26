<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>Dashboard - Cliente | AgendaSaaS</title>
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
    <h1>AgendaSaaS - Cliente</h1>
    <p>Bem-vindo, <strong>${cliente.nome}</strong></p>
</header>

<main>
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
</main>

<footer>
    &copy; 2025 AgendaSaaS
</footer>
</body>
</html>
