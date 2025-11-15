package br.cefet.agendasaas.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.springframework.stereotype.Service;

import br.cefet.agendasaas.dao.ClienteDAO;
import br.cefet.agendasaas.model.conexao.ConnectionFactory;
import br.cefet.agendasaas.model.entidades.Cliente;
import br.cefet.agendasaas.model.entidades.Prestador;
import br.cefet.agendasaas.model.enums.TipoUsuario;
import br.cefet.agendasaas.utils.InputValidator;
import br.cefet.agendasaas.utils.ValidationException;

@Service
public class CadastroService {

    public void cadastrarCliente(Cliente cliente) throws ValidationException {
        InputValidator.requireNonBlank(cliente.getNome(), "Nome");
        InputValidator.validateEmail(cliente.getEmail());
        InputValidator.requireNonBlank(cliente.getSenha(), "Senha");
        InputValidator.requireNonBlank(cliente.getCpf(), "CPF");

        ClienteDAO clienteDAO = new ClienteDAO();
        boolean sucesso = clienteDAO.inserir(cliente);
        if (!sucesso) {
            throw new ValidationException("Erro ao salvar cliente.");
        }
    }

    public void cadastrarPrestador(Prestador prestador) throws ValidationException {
        InputValidator.requireNonBlank(prestador.getNome(), "Nome");
        InputValidator.validateEmail(prestador.getEmail());
        InputValidator.requireNonBlank(prestador.getSenha(), "Senha");
        InputValidator.requireNonBlank(prestador.getTelefone(), "Telefone");
        InputValidator.requireNonBlank(prestador.getEspecializacao(), "Especialização");
        InputValidator.requireNonBlank(prestador.getCnpj(), "CNPJ");

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
            throw new ValidationException("Erro ao salvar prestador: " + e.getMessage());
        }
    }
}