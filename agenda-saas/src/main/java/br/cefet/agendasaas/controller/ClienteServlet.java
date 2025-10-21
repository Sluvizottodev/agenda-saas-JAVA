package br.cefet.agendasaas.controller;

import java.io.IOException;
import java.io.PrintWriter;

import br.cefet.agendasaas.dao.ClienteDAO;
import br.cefet.agendasaas.model.entidades.Cliente;
import br.cefet.agendasaas.model.enums.TipoUsuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "ClienteServlet", urlPatterns = {"/cliente"})
public class ClienteServlet extends GenericServlet {

    private final ClienteDAO clienteDAO = new ClienteDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");

    String nome = req.getParameter("nome");
    String email = req.getParameter("email");
    String senha = req.getParameter("senha");
    String cpf   = req.getParameter("cpf");

        try (PrintWriter out = resp.getWriter()) {

            // Validação básica
            if (nome == null || email == null || senha == null || cpf == null ||
                nome.trim().isEmpty() || email.trim().isEmpty() || senha.trim().isEmpty() || cpf.trim().isEmpty()) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    out.println("<h3>Erro: Todos os campos são obrigatórios!</h3>");
                return;
            }

            // Cria o objeto Cliente
            Cliente cliente = new Cliente();
            cliente.setNome(nome);
            cliente.setEmail(email);
            cliente.setSenha(senha);
            cliente.setCpf(cpf);
            cliente.setTipo(TipoUsuario.CLIENTE);

            // Tenta salvar no banco
            boolean sucesso = clienteDAO.inserir(cliente);

            if (sucesso) {
                resp.setStatus(HttpServletResponse.SC_CREATED);
                out.println("<h3>Cliente cadastrado com sucesso!</h3>");
                out.println("<p><strong>ID:</strong> " + cliente.getId() + "</p>");
                out.println("<p><strong>Nome:</strong> " + cliente.getNome() + "</p>");
            } else {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.println("<h3>Erro ao cadastrar cliente.</h3>");
            }
        }
    }
}

