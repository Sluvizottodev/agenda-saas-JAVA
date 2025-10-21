<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:set var="pageTitle" value="Cadastro - AgendaSaaS" />
<c:set var="pageRole" value="Auth" />
<c:set var="pageCss" value=".form-container{max-width:600px;margin:40px auto;background:#fff;padding:30px;border-radius:8px;box-shadow:var(--sombra-padrao);} .form-actions{text-align:center;} .hidden{display:none;}" />

<jsp:include page="/WEB-INF/views/includes/header.jsp" />

<script>
    function toggleCampos() {
        const tipo = document.getElementById("tipo").value;
        const clienteEl = document.getElementById("campos-cliente");
        const prestadorEl = document.getElementById("campos-prestador");
        if (clienteEl) clienteEl.style.display = (tipo === "cliente") ? "block" : "none";
        if (prestadorEl) prestadorEl.style.display = (tipo === "prestador") ? "block" : "none";
    }

    window.addEventListener("DOMContentLoaded", toggleCampos);
</script>

<main>
    <div class="form-container">
        <form action="${pageContext.request.contextPath}/auth/cadastro" method="post">
            <div class="form-group">
                <label for="tipo">Tipo de Usuário</label>
                <select id="tipo" name="tipo" onchange="toggleCampos()" required>
                    <option value="">Selecione...</option>
                    <option value="cliente">Cliente</option>
                    <option value="prestador">Prestador</option>
                </select>
            </div>

            <div class="form-group">
                <label for="nome">Nome completo</label>
                <input type="text" id="nome" name="nome" required>
            </div>

            <div class="form-group">
                <label for="email">E-mail</label>
                <input type="email" id="email" name="email" required>
            </div>

            <div class="form-group">
                <label for="senha">Senha</label>
                <input type="password" id="senha" name="senha" required>
            </div>

            <!-- Campos específicos do CLIENTE -->
            <div id="campos-cliente" class="hidden">
                <div class="form-group">
                    <label for="cpf">CPF</label>
                    <input type="text" id="cpf" name="cpf" placeholder="000.000.000-00">
                </div>
            </div>

            <!-- Campos específicos do PRESTADOR -->
            <div id="campos-prestador" class="hidden">
                <div class="form-group">
                    <label for="telefone">Telefone</label>
                    <input type="text" id="telefone" name="telefone">
                </div>
                <div class="form-group">
                    <label for="especializacao">Especialização</label>
                    <input type="text" id="especializacao" name="especializacao">
                </div>
                <div class="form-group">
                    <label for="cnpj">CNPJ</label>
                    <input type="text" id="cnpj" name="cnpj">
                </div>
            </div>

            <div class="form-actions">
                <button class="btn btn-lg" type="submit">Cadastrar</button>
            </div>
        </form>
        
        <p style="text-align:center; margin-top:15px;">
            Já tem conta? <a href="login.jsp">Fazer Login</a><br>
            <a href="${pageContext.request.contextPath}/" style="font-size: 0.9em; color: #666;">← Voltar à página inicial</a>
        </p>
    </div>
</main>

<jsp:include page="/WEB-INF/views/includes/footer.jsp" />

