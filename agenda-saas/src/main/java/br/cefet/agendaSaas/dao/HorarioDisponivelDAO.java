package br.cefet.agendaSaas.dao;

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

import br.cefet.agendaSaas.model.conexao.ConnectionFactory;
import br.cefet.agendaSaas.model.entidades.HorarioDisponivel;

public class HorarioDisponivelDAO {

    private final Connection con;

    public HorarioDisponivelDAO() {
        this.con = ConnectionFactory.getConnection();
    }

    /**
     * Insere um novo horário disponível no banco de dados
     */
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

    /**
     * Busca um horário disponível por ID
     */
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

    /**
     * Lista todos os horários disponíveis
     */
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

    /**
     * Lista horários disponíveis de um prestador específico
     */
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

    /**
     * Lista horários disponíveis de um prestador em uma data específica
     */
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

    /**
     * Lista apenas horários disponíveis (não ocupados) de um prestador
     */
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

    /**
     * Lista horários disponíveis em um período específico
     */
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

    /**
     * Verifica se existe conflito de horário para um prestador em uma data/hora
     * específica
     */
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

    /**
     * Atualiza um horário disponível
     */
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

    /**
     * Marca um horário como indisponível (ocupado)
     */
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

    /**
     * Marca um horário como disponível
     */
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

    /**
     * Remove um horário disponível
     */
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

    /**
     * Remove todos os horários de um prestador
     */
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

    /**
     * Remove horários antigos (anteriores à data atual)
     */
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

    /**
     * Gera horários automáticos para um prestador em um período
     */
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
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Método auxiliar para extrair HorarioDisponivel do ResultSet
     */
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
