package br.cefet.agendasaas.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import br.cefet.agendasaas.model.conexao.ConnectionFactory;
import br.cefet.agendasaas.model.entidades.Agendamento;

public class AgendamentoDAO {

    private final Connection con;

    public AgendamentoDAO() {
        this.con = ConnectionFactory.getConnection();
    }

    public boolean inserir(Agendamento agendamento) {
        String sql = "INSERT INTO agendamento (cliente_id, prestador_id, servico_id, data_hora, status) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, agendamento.getClienteId());
            stmt.setInt(2, agendamento.getPrestadorId());
            stmt.setInt(3, agendamento.getServicoId());
            stmt.setTimestamp(4, Timestamp.valueOf(agendamento.getDataHora()));
            stmt.setString(5, agendamento.getStatus());

            int rows = stmt.executeUpdate();
            if (rows == 0)
                return false;

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    agendamento.setId(rs.getInt(1));
                }
            }

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public Agendamento buscarPorId(int id) {
        String sql = "SELECT id, cliente_id, prestador_id, servico_id, data_hora, status FROM agendamento WHERE id = ?";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Agendamento agendamento = new Agendamento();
                    agendamento.setId(rs.getInt("id"));
                    agendamento.setClienteId(rs.getInt("cliente_id"));
                    agendamento.setPrestadorId(rs.getInt("prestador_id"));
                    agendamento.setServicoId(rs.getInt("servico_id"));

                    Timestamp timestamp = rs.getTimestamp("data_hora");
                    if (timestamp != null) {
                        agendamento.setDataHora(timestamp.toLocalDateTime());
                    }

                    agendamento.setStatus(rs.getString("status"));
                    return agendamento;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<Agendamento> listarPorPrestador(int prestadorId) {
        List<Agendamento> agendamentos = new ArrayList<>();
        String sql = "SELECT id, cliente_id, prestador_id, servico_id, data_hora, status FROM agendamento WHERE prestador_id = ?";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, prestadorId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Agendamento agendamento = new Agendamento();
                    agendamento.setId(rs.getInt("id"));
                    agendamento.setClienteId(rs.getInt("cliente_id"));
                    agendamento.setPrestadorId(rs.getInt("prestador_id"));
                    agendamento.setServicoId(rs.getInt("servico_id"));

                    Timestamp timestamp = rs.getTimestamp("data_hora");
                    if (timestamp != null) {
                        agendamento.setDataHora(timestamp.toLocalDateTime());
                    }

                    agendamento.setStatus(rs.getString("status"));
                    agendamentos.add(agendamento);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return agendamentos;
    }

    public List<Agendamento> listarPorCliente(int clienteId) {
        List<Agendamento> agendamentos = new ArrayList<>();
        String sql = "SELECT id, cliente_id, prestador_id, servico_id, data_hora, status FROM agendamento WHERE cliente_id = ?";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, clienteId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Agendamento agendamento = new Agendamento();
                    agendamento.setId(rs.getInt("id"));
                    agendamento.setClienteId(rs.getInt("cliente_id"));
                    agendamento.setPrestadorId(rs.getInt("prestador_id"));
                    agendamento.setServicoId(rs.getInt("servico_id"));

                    Timestamp timestamp = rs.getTimestamp("data_hora");
                    if (timestamp != null) {
                        agendamento.setDataHora(timestamp.toLocalDateTime());
                    }

                    agendamento.setStatus(rs.getString("status"));
                    agendamentos.add(agendamento);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return agendamentos;
    }

    public boolean atualizar(Agendamento agendamento) {
        String sql = "UPDATE agendamento SET cliente_id = ?, prestador_id = ?, servico_id = ?, data_hora = ?, status = ? WHERE id = ?";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, agendamento.getClienteId());
            stmt.setInt(2, agendamento.getPrestadorId());
            stmt.setInt(3, agendamento.getServicoId());
            stmt.setTimestamp(4, Timestamp.valueOf(agendamento.getDataHora()));
            stmt.setString(5, agendamento.getStatus());
            stmt.setInt(6, agendamento.getId());

            int rows = stmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean remover(int id) {
        String sql = "DELETE FROM agendamento WHERE id = ?";

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

