package br.cefet.agendasaas.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import br.cefet.agendasaas.dao.ClienteDAO;
import br.cefet.agendasaas.model.conexao.ConnectionFactory;
import br.cefet.agendasaas.model.entidades.Cliente;
import br.cefet.agendasaas.model.entidades.Prestador;
import br.cefet.agendasaas.model.enums.TipoUsuario;
import br.cefet.agendasaas.utils.InputValidator;
import br.cefet.agendasaas.utils.ValidationException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/auth/cadastro")
public class CadastroServlet extends GenericServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            String tipoUsuario = InputValidator.requireNonBlank(request.getParameter("tipo"), "Tipo de usuário");
            String nome = InputValidator.requireNonBlank(request.getParameter("nome"), "Nome");
            String email = InputValidator.validateEmail(request.getParameter("email"));
            String senha = InputValidator.requireNonBlank(request.getParameter("senha"), "Senha");

            if ("cliente".equalsIgnoreCase(tipoUsuario)) {
                String cpf = InputValidator.requireNonBlank(request.getParameter("cpf"), "CPF");

                Cliente cliente = new Cliente();
                cliente.setNome(nome);
                cliente.setEmail(email);
                cliente.setSenha(senha);
                cliente.setCpf(cpf);

                ClienteDAO clienteDAO = new ClienteDAO();
                boolean sucesso = clienteDAO.inserir(cliente);
                if (!sucesso) {
                    request.setAttribute("erro", "Erro ao salvar cliente.");
                    request.getRequestDispatcher("cadastro.jsp").forward(request, response);
                    return;
                }
                System.out.println("Cliente cadastrado: " + cliente.getNome());

            } else if ("prestador".equalsIgnoreCase(tipoUsuario)) {
                String telefone = InputValidator.requireNonBlank(request.getParameter("telefone"), "Telefone");
                String especializacao = InputValidator.requireNonBlank(request.getParameter("especializacao"),
                        "Especialização");
                String cnpj = InputValidator.requireNonBlank(request.getParameter("cnpj"), "CNPJ");

                Prestador prestador = new Prestador();
                prestador.setNome(nome);
                prestador.setEmail(email);
                prestador.setSenha(senha);
                prestador.setTelefone(telefone);
                prestador.setEspecializacao(especializacao);
                prestador.setCnpj(cnpj);

                String sqlUsuario = "INSERT INTO usuario (nome, email, senha, tipo) VALUES (?, ?, ?, ?)";
                String sqlPrestador = "INSERT INTO prestador (id, telefone, especializacao, cnpj) VALUES (?, ?, ?, ?)";

                try (Connection con = ConnectionFactory.getConnection();
                        PreparedStatement stmtUser = con.prepareStatement(sqlUsuario,
                                java.sql.Statement.RETURN_GENERATED_KEYS)) {

                    stmtUser.setString(1, prestador.getNome());
                    stmtUser.setString(2, prestador.getEmail());
                    stmtUser.setString(3, prestador.getSenha());
                    stmtUser.setString(4, TipoUsuario.PRESTADOR.name());

                    int rows = stmtUser.executeUpdate();
                    if (rows == 0) {
                        throw new SQLException("Falha ao inserir usuario");
                    }

                    try (java.sql.ResultSet rs = stmtUser.getGeneratedKeys()) {
                        if (rs.next()) {
                            int idGerado = rs.getInt(1);
                            try (PreparedStatement stmtPrest = con.prepareStatement(sqlPrestador)) {
                                stmtPrest.setInt(1, idGerado);
                                stmtPrest.setString(2, prestador.getTelefone());
                                stmtPrest.setString(3, prestador.getEspecializacao());
                                stmtPrest.setString(4, prestador.getCnpj());
                                int pr = stmtPrest.executeUpdate();
                                if (pr == 0) {
                                    throw new SQLException("Falha ao inserir prestador");
                                }
                            }
                        } else {
                            throw new SQLException("ID gerado nao retornado");
                        }
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                    request.setAttribute("erro", "Erro ao salvar prestador: " + e.getMessage());
                    request.getRequestDispatcher("cadastro.jsp").forward(request, response);
                    return;
                }

                System.out.println("Prestador cadastrado: " + prestador.getNome());

            } else {
                request.setAttribute("erro", "Tipo de usuário inválido.");
                request.getRequestDispatcher("cadastro.jsp").forward(request, response);
                return;
            }

        } catch (ValidationException ve) {
            request.setAttribute("erro", ve.getMessage());
            request.getRequestDispatcher("cadastro.jsp").forward(request, response);
            return;
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("erro", "Erro interno ao processar cadastro. Tente novamente mais tarde.");
            request.getRequestDispatcher("cadastro.jsp").forward(request, response);
            return;
        }

        request.setAttribute("mensagem", "Cadastro realizado com sucesso!");
        request.getRequestDispatcher("login.jsp").forward(request, response);
    }
}
