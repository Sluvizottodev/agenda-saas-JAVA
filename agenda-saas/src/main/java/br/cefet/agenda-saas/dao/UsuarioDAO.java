package br.cefet.agendaSaas.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import br.cefet.agendaSaas.model.conexao.ConnectionFactory;
import br.cefet.agendaSaas.model.entidades.Cliente;
import br.cefet.agendaSaas.model.entidades.Prestador;
import br.cefet.agendaSaas.model.entidades.Usuario;
import br.cefet.agendaSaas.model.enums.TipoUsuario;

public class UsuarioDAO {

    private final Connection con;

    public UsuarioDAO() {
        this.con = ConnectionFactory.getConnection();
    }

    public boolean inserir(Usuario usuario) {
        String sql = "INSERT INTO usuario (nome, email, senha, tipo) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getSenha());
            stmt.setString(4, usuario.getTipo().name());

            int rows = stmt.executeUpdate();
            if (rows == 0)
                return false;

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    usuario.setId(rs.getInt(1));
                }
            }

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public Usuario buscarPorId(int id) {
        String sql = "SELECT id, nome, email, senha, tipo FROM usuario WHERE id = ?";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    TipoUsuario tipo = TipoUsuario.valueOf(rs.getString("tipo"));
                    Usuario usuario = criarUsuarioPorTipo(tipo);

                    usuario.setId(rs.getInt("id"));
                    usuario.setNome(rs.getString("nome"));
                    usuario.setEmail(rs.getString("email"));
                    usuario.setSenha(rs.getString("senha"));
                    usuario.setTipo(tipo);
                    return usuario;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Usuario buscarPorEmailSenha(String email, String senha) {
        String sql = "SELECT id, nome, email, senha, tipo FROM usuario WHERE email = ? AND senha = ?";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, email);
            stmt.setString(2, senha);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    TipoUsuario tipo = TipoUsuario.valueOf(rs.getString("tipo"));
                    Usuario usuario = criarUsuarioPorTipo(tipo);

                    usuario.setId(rs.getInt("id"));
                    usuario.setNome(rs.getString("nome"));
                    usuario.setEmail(rs.getString("email"));
                    usuario.setSenha(rs.getString("senha"));
                    usuario.setTipo(tipo);
                    return usuario;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<Usuario> listarTodos() {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT id, nome, email, senha, tipo FROM usuario";

        try (
                PreparedStatement stmt = con.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                TipoUsuario tipo = TipoUsuario.valueOf(rs.getString("tipo"));
                Usuario usuario = criarUsuarioPorTipo(tipo);

                usuario.setId(rs.getInt("id"));
                usuario.setNome(rs.getString("nome"));
                usuario.setEmail(rs.getString("email"));
                usuario.setSenha(rs.getString("senha"));
                usuario.setTipo(tipo);
                usuarios.add(usuario);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return usuarios;
    }

    public boolean atualizar(Usuario usuario) {
        String sql = "UPDATE usuario SET nome = ?, email = ?, senha = ?, tipo = ? WHERE id = ?";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getSenha());
            stmt.setString(4, usuario.getTipo().name());
            stmt.setInt(5, usuario.getId());

            int rows = stmt.executeUpdate();
            return rows > 0;

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

    private Usuario criarUsuarioPorTipo(TipoUsuario tipo) {
        switch (tipo) {
            case CLIENTE:
                return new Cliente();
            case PRESTADOR:
                return new Prestador();
            case ADMIN:
                // Para admin, vamos criar um Cliente por padrão por enquanto
                // ou você pode criar uma classe Admin separada
                Cliente admin = new Cliente();
                admin.setTipo(TipoUsuario.ADMIN);
                return admin;
            default:
                throw new IllegalArgumentException("Tipo de usuário inválido: " + tipo);
        }
    }
}
