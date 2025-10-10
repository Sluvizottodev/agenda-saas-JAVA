package br.cefet.agendaSaas.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import br.cefet.agendaSaas.model.conexao.ConnectionFactory;
import br.cefet.agendaSaas.model.entidades.Servico;

public class ServicoDAO {

    private final Connection con;

    public ServicoDAO() {
        this.con = ConnectionFactory.getConnection();
    }

    public boolean inserir(Servico servico) {
        String sql = "INSERT INTO servico (nome, descricao, preco, prestador_id) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, servico.getNome());
            stmt.setString(2, servico.getDescricao());
            stmt.setDouble(3, servico.getPreco());
            stmt.setInt(4, servico.getPrestadorId());

            int rows = stmt.executeUpdate();
            if (rows == 0)
                return false;

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    servico.setId(rs.getInt(1));
                }
            }

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public Servico buscarPorId(int id) {
        String sql = "SELECT id, nome, descricao, preco, prestador_id FROM servico WHERE id = ?";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Servico servico = new Servico();
                    servico.setId(rs.getInt("id"));
                    servico.setNome(rs.getString("nome"));
                    servico.setDescricao(rs.getString("descricao"));
                    servico.setPreco(rs.getDouble("preco"));
                    servico.setPrestadorId(rs.getInt("prestador_id"));
                    return servico;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<Servico> listarTodos() {
        List<Servico> servicos = new ArrayList<>();
        String sql = "SELECT id, nome, descricao, preco, prestador_id FROM servico";

        try (
                PreparedStatement stmt = con.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Servico servico = new Servico();
                servico.setId(rs.getInt("id"));
                servico.setNome(rs.getString("nome"));
                servico.setDescricao(rs.getString("descricao"));
                servico.setPreco(rs.getDouble("preco"));
                servico.setPrestadorId(rs.getInt("prestador_id"));
                servicos.add(servico);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return servicos;
    }

    public boolean atualizar(Servico servico) {
        String sql = "UPDATE servico SET nome = ?, descricao = ?, preco = ?, prestador_id = ? WHERE id = ?";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, servico.getNome());
            stmt.setString(2, servico.getDescricao());
            stmt.setDouble(3, servico.getPreco());
            stmt.setInt(4, servico.getPrestadorId());
            stmt.setInt(5, servico.getId());

            int rows = stmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean remover(int id) {
        String sql = "DELETE FROM servico WHERE id = ?";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public List<Servico> listarPorPrestador(int prestadorId) {
        List<Servico> servicos = new ArrayList<>();
        String sql = "SELECT id, nome, descricao, preco, prestador_id FROM servico WHERE prestador_id = ?";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, prestadorId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Servico servico = new Servico();
                    servico.setId(rs.getInt("id"));
                    servico.setNome(rs.getString("nome"));
                    servico.setDescricao(rs.getString("descricao"));
                    servico.setPreco(rs.getDouble("preco"));
                    servico.setPrestadorId(rs.getInt("prestador_id"));
                    servicos.add(servico);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return servicos;
    }
}
