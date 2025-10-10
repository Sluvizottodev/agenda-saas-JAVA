package br.cefet.agendaSaas.dao;

import br.cefet.agendaSaas.model.conexao.ConnectionFactory;
import br.cefet.agendaSaas.model.entidades.Cliente;
import br.cefet.agendaSaas.model.enums.TipoUsuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    private final Connection con;

    public ClienteDAO() {
        this.con = ConnectionFactory.getConnection();
    }

    public boolean inserir(Cliente cliente) {
        String sqlUsuario = "INSERT INTO usuario (nome, email, senha, tipo) VALUES (?, ?, ?, ?)";
        String sqlCliente = "INSERT INTO cliente (id, cpf) VALUES (?, ?)";

        try (
                PreparedStatement stmtUsuario = con.prepareStatement(sqlUsuario, Statement.RETURN_GENERATED_KEYS)) {
            stmtUsuario.setString(1, cliente.getNome());
            stmtUsuario.setString(2, cliente.getEmail());
            stmtUsuario.setString(3, cliente.getSenha());
            stmtUsuario.setString(4, TipoUsuario.CLIENTE.name());

            int rowsAffected = stmtUsuario.executeUpdate();

            if (rowsAffected == 0) {
                return false;
            }

            try (ResultSet generatedKeys = stmtUsuario.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int idGerado = generatedKeys.getInt(1);
                    cliente.setId(idGerado);

                    try (PreparedStatement stmtCliente = con.prepareStatement(sqlCliente)) {
                        stmtCliente.setInt(1, idGerado);
                        stmtCliente.setString(2, cliente.getCpf());

                        int clienteRows = stmtCliente.executeUpdate();
                        return clienteRows > 0;
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public Cliente buscarPorId(int id) {
        String sql = "SELECT u.id, u.nome, u.email, u.senha, u.tipo, c.cpf " +
                "FROM usuario u " +
                "JOIN cliente c ON u.id = c.id " +
                "WHERE u.id = ?";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Cliente cliente = new Cliente();
                    cliente.setId(rs.getInt("id"));
                    cliente.setNome(rs.getString("nome"));
                    cliente.setEmail(rs.getString("email"));
                    cliente.setSenha(rs.getString("senha"));
                    cliente.setCpf(rs.getString("cpf"));

                    String tipo = rs.getString("tipo");
                    cliente.setTipo(TipoUsuario.valueOf(tipo));

                    return cliente;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<Cliente> listarTodos() {
        List<Cliente> clientes = new ArrayList<>();

        String sql = "SELECT u.id, u.nome, u.email, u.senha, u.tipo, c.cpf " +
                "FROM usuario u " +
                "JOIN cliente c ON u.id = c.id";

        try (
                PreparedStatement stmt = con.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Cliente cliente = new Cliente();
                cliente.setId(rs.getInt("id"));
                cliente.setNome(rs.getString("nome"));
                cliente.setEmail(rs.getString("email"));
                cliente.setSenha(rs.getString("senha"));
                cliente.setCpf(rs.getString("cpf"));
                cliente.setTipo(TipoUsuario.valueOf(rs.getString("tipo")));

                clientes.add(cliente);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return clientes;
    }

    public boolean atualizar(Cliente cliente) {
        String sqlUsuario = "UPDATE usuario SET nome = ?, email = ?, senha = ? WHERE id = ?";
        String sqlCliente = "UPDATE cliente SET cpf = ? WHERE id = ?";

        try (
                PreparedStatement stmtUsuario = con.prepareStatement(sqlUsuario);
                PreparedStatement stmtCliente = con.prepareStatement(sqlCliente)) {
            stmtUsuario.setString(1, cliente.getNome());
            stmtUsuario.setString(2, cliente.getEmail());
            stmtUsuario.setString(3, cliente.getSenha());
            stmtUsuario.setInt(4, cliente.getId());

            stmtCliente.setString(1, cliente.getCpf());
            stmtCliente.setInt(2, cliente.getId());

            int rowsUsuario = stmtUsuario.executeUpdate();
            int rowsCliente = stmtCliente.executeUpdate();

            return rowsUsuario > 0 && rowsCliente > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean remover(int id) {
        String sql = "DELETE FROM usuario WHERE id = ?";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int rows = stmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}
