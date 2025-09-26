<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>AgendaSaaS - Agendamentos Online</title>
    <link rel="stylesheet" href="style.css">
    <style>
        main {
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            padding: 40px 20px;
            background-color: var(--cor-fundo);
        }

        .cta {
            text-align: center;
            margin-bottom: 30px;
            color: var(--cor-texto);
        }

        .cta h2 {
            margin-bottom: 10px;
            font-size: 1.8em;
            color: var(--cor-primaria);
        }

        .cta p {
            margin-bottom: 20px;
            font-size: 1em;
            color: #555;
        }

        .acoes {
            display: flex;
            gap: 20px;
            justify-content: center;
            flex-wrap: wrap;
        }

        .btn-lg {
            font-size: 1.1em;
            padding: 14px 28px;
        }

        footer {
            text-align: center;
            padding: 20px;
            font-size: 0.9em;
            color: #888;
            background-color: #f0f0f0;
            margin-top: 40px;
        }

        @media (max-width: 600px) {
            .btn-lg {
                width: 100%;
                text-align: center;
            }

            .acoes {
                flex-direction: column;
                align-items: stretch;
            }
        }
    </style>
</head>
<body>
    <header>
        <h1>AgendaSaaS</h1>
        <p>Sua plataforma moderna de agendamento online</p>
    </header>

    <main>
        <div class="cta">
            <h2>Seja bem-vindo!</h2>
            <p>Escolha abaixo como deseja continuar:</p>
        </div>

        <div class="acoes">
            <a class="btn btn-lg" href="auth/login.jsp">Fazer Login</a>
            <a class="btn btn-lg" href="auth/cadastro.jsp">Criar Conta</a>
        </div>
    </main>

    <footer>
        &copy; 2025 AgendaSaaS.
    </footer>
</body>
</html>
