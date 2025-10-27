package br.cefet.agendasaas.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import br.cefet.agendasaas.model.conexao.ConnectionFactory;
import br.cefet.agendasaas.model.entidades.HorarioDisponivel;

public class HorarioDisponivelDAO {

    private final Connection con;

    public HorarioDisponivelDAO() {
        this.con = ConnectionFactory.getConnection();
    }

 
    public boolean inserir(HorarioDisponivel horario) {
        String sql = "INSERT INTO horario_disponivel (prestador_id, data, hora_inicio, hora_fim, disponivel) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, horario.getPrestadorId());
            stmt.setDate(2, Date.valueOf(horario.getData()));
            stmt.setTime(3, Time.valueOf(horario.getHoraInicio()));
            stmt.setTime(4, Time.valueOf(horario.getHoraFim()));
            stmt.setBoolean(5, horario.isDisponivel());

            int rows = stmt.executeUpdate();
            if (rows == 0)
                return false;

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    horario.setId(rs.getInt(1));
                }
            }

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public HorarioDisponivel buscarPorId(int id) {
        String sql = "SELECT id, prestador_id, data, hora_inicio, hora_fim, disponivel FROM horario_disponivel WHERE id = ?";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extrairHorarioDoResultSet(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<HorarioDisponivel> listarTodos() {
        List<HorarioDisponivel> horarios = new ArrayList<>();
        String sql = "SELECT id, prestador_id, data, hora_inicio, hora_fim, disponivel FROM horario_disponivel ORDER BY data, hora_inicio";

        try (PreparedStatement stmt = con.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                horarios.add(extrairHorarioDoResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return horarios;
    }

    public List<HorarioDisponivel> listarPorPrestador(int prestadorId) {
        List<HorarioDisponivel> horarios = new ArrayList<>();
        String sql = "SELECT id, prestador_id, data, hora_inicio, hora_fim, disponivel FROM horario_disponivel WHERE prestador_id = ? ORDER BY data, hora_inicio";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, prestadorId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    horarios.add(extrairHorarioDoResultSet(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return horarios;
    }

    public List<HorarioDisponivel> listarPorPrestadorEData(int prestadorId, LocalDate data) {
        List<HorarioDisponivel> horarios = new ArrayList<>();
        String sql = "SELECT id, prestador_id, data, hora_inicio, hora_fim, disponivel FROM horario_disponivel WHERE prestador_id = ? AND data = ? ORDER BY hora_inicio";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, prestadorId);
            stmt.setDate(2, Date.valueOf(data));

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    horarios.add(extrairHorarioDoResultSet(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return horarios;
    }

    public List<HorarioDisponivel> listarDisponiveisPorPrestador(int prestadorId) {
        List<HorarioDisponivel> horarios = new ArrayList<>();
        String sql = "SELECT id, prestador_id, data, hora_inicio, hora_fim, disponivel FROM horario_disponivel WHERE prestador_id = ? AND disponivel = true AND data >= CURDATE() ORDER BY data, hora_inicio";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, prestadorId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    horarios.add(extrairHorarioDoResultSet(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return horarios;
    }

    public List<HorarioDisponivel> listarPorPeriodo(LocalDate dataInicio, LocalDate dataFim) {
        List<HorarioDisponivel> horarios = new ArrayList<>();
        String sql = "SELECT id, prestador_id, data, hora_inicio, hora_fim, disponivel FROM horario_disponivel WHERE data BETWEEN ? AND ? ORDER BY prestador_id, data, hora_inicio";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(dataInicio));
            stmt.setDate(2, Date.valueOf(dataFim));

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    horarios.add(extrairHorarioDoResultSet(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return horarios;
    }

    public boolean verificarConflito(int prestadorId, LocalDate data, LocalTime horaInicio, LocalTime horaFim) {
        String sql = "SELECT COUNT(*) FROM horario_disponivel WHERE prestador_id = ? AND data = ? AND ((hora_inicio <= ? AND hora_fim > ?) OR (hora_inicio < ? AND hora_fim >= ?) OR (hora_inicio >= ? AND hora_fim <= ?))";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, prestadorId);
            stmt.setDate(2, Date.valueOf(data));
            stmt.setTime(3, Time.valueOf(horaInicio));
            stmt.setTime(4, Time.valueOf(horaInicio));
            stmt.setTime(5, Time.valueOf(horaFim));
            stmt.setTime(6, Time.valueOf(horaFim));
            stmt.setTime(7, Time.valueOf(horaInicio));
            stmt.setTime(8, Time.valueOf(horaFim));

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean atualizar(HorarioDisponivel horario) {
        String sql = "UPDATE horario_disponivel SET prestador_id = ?, data = ?, hora_inicio = ?, hora_fim = ?, disponivel = ? WHERE id = ?";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, horario.getPrestadorId());
            stmt.setDate(2, Date.valueOf(horario.getData()));
            stmt.setTime(3, Time.valueOf(horario.getHoraInicio()));
            stmt.setTime(4, Time.valueOf(horario.getHoraFim()));
            stmt.setBoolean(5, horario.isDisponivel());
            stmt.setInt(6, horario.getId());

            int rows = stmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean marcarComoIndisponivel(int id) {
        String sql = "UPDATE horario_disponivel SET disponivel = false WHERE id = ?";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean marcarComoDisponivel(int id) {
        String sql = "UPDATE horario_disponivel SET disponivel = true WHERE id = ?";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean remover(int id) {
        String sql = "DELETE FROM horario_disponivel WHERE id = ?";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean removerPorPrestador(int prestadorId) {
        String sql = "DELETE FROM horario_disponivel WHERE prestador_id = ?";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, prestadorId);
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean limparHorariosAntigos() {
        String sql = "DELETE FROM horario_disponivel WHERE data < CURDATE()";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean gerarHorariosAutomaticos(int prestadorId, LocalDate dataInicio, LocalDate dataFim,
            LocalTime horaInicioTrabalho, LocalTime horaFimTrabalho,
            int intervalosMinutos) {
        try {
            boolean sucesso = true;
            LocalDate dataAtual = dataInicio;

            while (!dataAtual.isAfter(dataFim)) {
                LocalTime horaAtual = horaInicioTrabalho;

                while (horaAtual.isBefore(horaFimTrabalho)) {
                    LocalTime horaFim = horaAtual.plusMinutes(intervalosMinutos);

                    if (horaFim.isAfter(horaFimTrabalho)) {
                        break;
                    }

                    HorarioDisponivel horario = new HorarioDisponivel(prestadorId, dataAtual, horaAtual, horaFim);

                    if (!inserir(horario)) {
                        sucesso = false;
                    }

                    horaAtual = horaFim;
                }

                dataAtual = dataAtual.plusDays(1);
            }

            return sucesso;
        } catch (RuntimeException e) {
            e.printStackTrace();
            return false;
        }
    }

    private HorarioDisponivel extrairHorarioDoResultSet(ResultSet rs) throws SQLException {
        HorarioDisponivel horario = new HorarioDisponivel();
        horario.setId(rs.getInt("id"));
        horario.setPrestadorId(rs.getInt("prestador_id"));

        Date data = rs.getDate("data");
        if (data != null) {
            horario.setData(data.toLocalDate());
        }

        Time horaInicio = rs.getTime("hora_inicio");
        if (horaInicio != null) {
            horario.setHoraInicio(horaInicio.toLocalTime());
        }

        Time horaFim = rs.getTime("hora_fim");
        if (horaFim != null) {
            horario.setHoraFim(horaFim.toLocalTime());
        }

        horario.setDisponivel(rs.getBoolean("disponivel"));

        return horario;
    }
}

