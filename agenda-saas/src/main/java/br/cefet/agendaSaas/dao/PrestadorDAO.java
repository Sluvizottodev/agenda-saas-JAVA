package br.cefet.agendaSaas.dao;

import br.cefet.agendaSaas.model.conexao.ConnectionFactory;
import br.cefet.agendaSaas.model.entidades.Prestador;
import br.cefet.agendaSaas.model.enums.TipoUsuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PrestadorDAO {

    private final Connection con;

    public PrestadorDAO() {
        this.con = ConnectionFactory.getConnection();
    }

    public boolean inserir(Prestador prestador) {
        String sqlUsuario = "INSERT INTO usuario (nome, email, senha, tipo) VALUES (?, ?, ?, ?)";
        String sqlPrestador = "INSERT INTO prestador (id, cnpj, telefone, especializacao) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmtUsuario = con.prepareStatement(sqlUsuario, Statement.RETURN_GENERATED_KEYS)) {
            stmtUsuario.setString(1, prestador.getNome());
            stmtUsuario.setString(2, prestador.getEmail());
            stmtUsuario.setString(3, prestador.getSenha());
            stmtUsuario.setString(4, TipoUsuario.PRESTADOR.name());

            int rowsAffected = stmtUsuario.executeUpdate();

            if (rowsAffected == 0) {
                return false;
            }

            try (ResultSet generatedKeys = stmtUsuario.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int idGerado = generatedKeys.getInt(1);
                    prestador.setId(idGerado);

                    try (PreparedStatement stmtPrestador = con.prepareStatement(sqlPrestador)) {
                        stmtPrestador.setInt(1, idGerado);
                        stmtPrestador.setString(2, prestador.getCnpj());
                        stmtPrestador.setString(3, prestador.getTelefone());
                        stmtPrestador.setString(4, prestador.getEspecializacao());

                        int prestadorRows = stmtPrestador.executeUpdate();
                        return prestadorRows > 0;
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Em produção, use um Logger
        }

        return false;
    }

    public Prestador buscarPorId(int id) {
        String sql = "SELECT u.id, u.nome, u.email, u.senha, u.tipo, p.cnpj, p.telefone, p.especializacao " +
                "FROM usuario u " +
                "JOIN prestador p ON u.id = p.id " +
                "WHERE u.id = ?";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Prestador prestador = new Prestador();
                    prestador.setId(rs.getInt("id"));
                    prestador.setNome(rs.getString("nome"));
                    prestador.setEmail(rs.getString("email"));
                    prestador.setSenha(rs.getString("senha"));
                    prestador.setCnpj(rs.getString("cnpj"));
                    prestador.setTelefone(rs.getString("telefone"));
                    prestador.setEspecializacao(rs.getString("especializacao"));

                    String tipo = rs.getString("tipo");
                    prestador.setTipo(TipoUsuario.valueOf(tipo));

                    return prestador;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<Prestador> listarTodos() {
        List<Prestador> prestadores = new ArrayList<>();

        String sql = "SELECT u.id, u.nome, u.email, u.senha, u.tipo, p.cnpj, p.telefone, p.especializacao " +
                "FROM usuario u " +
                "JOIN prestador p ON u.id = p.id";

        try (
                PreparedStatement stmt = con.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Prestador prestador = new Prestador();
                prestador.setId(rs.getInt("id"));
                prestador.setNome(rs.getString("nome"));
                prestador.setEmail(rs.getString("email"));
                prestador.setSenha(rs.getString("senha"));
                prestador.setCnpj(rs.getString("cnpj"));
                prestador.setTelefone(rs.getString("telefone"));
                prestador.setEspecializacao(rs.getString("especializacao"));
                prestador.setTipo(TipoUsuario.valueOf(rs.getString("tipo")));

                prestadores.add(prestador);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return prestadores;
    }

    public boolean atualizar(Prestador prestador) {
        String sqlUsuario = "UPDATE usuario SET nome = ?, email = ?, senha = ? WHERE id = ?";
        String sqlPrestador = "UPDATE prestador SET cnpj = ?, telefone = ?, especializacao = ? WHERE id = ?";

        try (
                PreparedStatement stmtUsuario = con.prepareStatement(sqlUsuario);
                PreparedStatement stmtPrestador = con.prepareStatement(sqlPrestador)) {
            stmtUsuario.setString(1, prestador.getNome());
            stmtUsuario.setString(2, prestador.getEmail());
            stmtUsuario.setString(3, prestador.getSenha());
            stmtUsuario.setInt(4, prestador.getId());

            stmtPrestador.setString(1, prestador.getCnpj());
            stmtPrestador.setString(2, prestador.getTelefone());
            stmtPrestador.setString(3, prestador.getEspecializacao());
            stmtPrestador.setInt(4, prestador.getId());

            int rowsUsuario = stmtUsuario.executeUpdate();
            int rowsPrestador = stmtPrestador.executeUpdate();

            return rowsUsuario > 0 && rowsPrestador > 0;

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

    public Prestador buscarPorCnpj(String cnpj) {
        String sql = "SELECT u.id, u.nome, u.email, u.senha, u.tipo, p.cnpj, p.telefone, p.especializacao " +
                "FROM usuario u " +
                "JOIN prestador p ON u.id = p.id " +
                "WHERE p.cnpj = ?";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, cnpj);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Prestador prestador = new Prestador();
                    prestador.setId(rs.getInt("id"));
                    prestador.setNome(rs.getString("nome"));
                    prestador.setEmail(rs.getString("email"));
                    prestador.setSenha(rs.getString("senha"));
                    prestador.setCnpj(rs.getString("cnpj"));
                    prestador.setTelefone(rs.getString("telefone"));
                    prestador.setEspecializacao(rs.getString("especializacao"));
                    prestador.setTipo(TipoUsuario.valueOf(rs.getString("tipo")));

                    return prestador;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}