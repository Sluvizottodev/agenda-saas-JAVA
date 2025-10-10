package br.cefet.agendaSaas.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import br.cefet.agendaSaas.dao.HorarioDisponivelDAO;
import br.cefet.agendaSaas.model.entidades.HorarioDisponivel;

public class HorarioService {

    private final HorarioDisponivelDAO horarioDAO;

    public HorarioService() {
        this.horarioDAO = new HorarioDisponivelDAO();
    }

    /**
     * Cadastra um novo horário disponível
     */
    public boolean cadastrarHorario(HorarioDisponivel horario) {
        try {
            // Validações de negócio
            if (horario.getData().isBefore(LocalDate.now())) {
                throw new IllegalArgumentException("Não é possível cadastrar horários no passado");
            }

            if (horario.getHoraInicio().isAfter(horario.getHoraFim())) {
                throw new IllegalArgumentException("Hora de início deve ser anterior à hora de fim");
            }

            // Verifica se já existe conflito de horário
            if (horarioDAO.verificarConflito(horario.getPrestadorId(), horario.getData(),
                    horario.getHoraInicio(), horario.getHoraFim())) {
                throw new IllegalArgumentException("Já existe um horário conflitante cadastrado");
            }

            return horarioDAO.inserir(horario);

        } catch (Exception e) {
            System.err.println("Erro ao cadastrar horário: " + e.getMessage());
            return false;
        }
    }

    /**
     * Busca horários disponíveis de um prestador
     */
    public List<HorarioDisponivel> buscarHorariosDisponiveisPorPrestador(int prestadorId) {
        return horarioDAO.listarDisponiveisPorPrestador(prestadorId);
    }

    /**
     * Busca horários de um prestador em uma data específica
     */
    public List<HorarioDisponivel> buscarHorariosPorPrestadorEData(int prestadorId, LocalDate data) {
        return horarioDAO.listarPorPrestadorEData(prestadorId, data);
    }

    /**
     * Busca apenas horários disponíveis (não ocupados) de um prestador em uma data
     * específica
     */
    public List<HorarioDisponivel> buscarHorariosDisponiveisPorPrestadorEData(int prestadorId, LocalDate data) {
        return horarioDAO.listarPorPrestadorEData(prestadorId, data)
                .stream()
                .filter(HorarioDisponivel::isDisponivel)
                .filter(horario -> !horario.isHorarioPassado())
                .collect(Collectors.toList());
    }

    /**
     * Reserva um horário (marca como indisponível)
     */
    public boolean reservarHorario(int horarioId) {
        try {
            HorarioDisponivel horario = horarioDAO.buscarPorId(horarioId);

            if (horario == null) {
                throw new IllegalArgumentException("Horário não encontrado");
            }

            if (!horario.isDisponivel()) {
                throw new IllegalArgumentException("Horário já está ocupado");
            }

            if (horario.isHorarioPassado()) {
                throw new IllegalArgumentException("Não é possível reservar horários no passado");
            }

            return horarioDAO.marcarComoIndisponivel(horarioId);

        } catch (Exception e) {
            System.err.println("Erro ao reservar horário: " + e.getMessage());
            return false;
        }
    }

    /**
     * Libera um horário (marca como disponível)
     */
    public boolean liberarHorario(int horarioId) {
        try {
            HorarioDisponivel horario = horarioDAO.buscarPorId(horarioId);

            if (horario == null) {
                throw new IllegalArgumentException("Horário não encontrado");
            }

            return horarioDAO.marcarComoDisponivel(horarioId);

        } catch (Exception e) {
            System.err.println("Erro ao liberar horário: " + e.getMessage());
            return false;
        }
    }

    /**
     * Atualiza um horário disponível
     */
    public boolean atualizarHorario(HorarioDisponivel horario) {
        try {
            // Validações de negócio
            if (horario.getId() <= 0) {
                throw new IllegalArgumentException("ID do horário é obrigatório para atualização");
            }

            HorarioDisponivel horarioExistente = horarioDAO.buscarPorId(horario.getId());
            if (horarioExistente == null) {
                throw new IllegalArgumentException("Horário não encontrado");
            }

            if (horario.getHoraInicio().isAfter(horario.getHoraFim())) {
                throw new IllegalArgumentException("Hora de início deve ser anterior à hora de fim");
            }

            return horarioDAO.atualizar(horario);

        } catch (Exception e) {
            System.err.println("Erro ao atualizar horário: " + e.getMessage());
            return false;
        }
    }

    /**
     * Remove um horário
     */
    public boolean removerHorario(int horarioId) {
        try {
            HorarioDisponivel horario = horarioDAO.buscarPorId(horarioId);

            if (horario == null) {
                throw new IllegalArgumentException("Horário não encontrado");
            }

            return horarioDAO.remover(horarioId);

        } catch (Exception e) {
            System.err.println("Erro ao remover horário: " + e.getMessage());
            return false;
        }
    }

    /**
     * Gera horários automáticos para um prestador
     */
    public boolean gerarHorariosAutomaticos(int prestadorId, LocalDate dataInicio, LocalDate dataFim,
            LocalTime horaInicioTrabalho, LocalTime horaFimTrabalho,
            int intervalosMinutos) {
        try {
            // Validações
            if (dataInicio.isBefore(LocalDate.now())) {
                throw new IllegalArgumentException("Data de início não pode ser no passado");
            }

            if (dataFim.isBefore(dataInicio)) {
                throw new IllegalArgumentException("Data de fim deve ser posterior à data de início");
            }

            if (horaInicioTrabalho.isAfter(horaFimTrabalho)) {
                throw new IllegalArgumentException("Hora de início do trabalho deve ser anterior à hora de fim");
            }

            if (intervalosMinutos <= 0 || intervalosMinutos > 240) {
                throw new IllegalArgumentException("Intervalo deve ser entre 1 e 240 minutos");
            }

            return horarioDAO.gerarHorariosAutomaticos(prestadorId, dataInicio, dataFim,
                    horaInicioTrabalho, horaFimTrabalho,
                    intervalosMinutos);

        } catch (Exception e) {
            System.err.println("Erro ao gerar horários automáticos: " + e.getMessage());
            return false;
        }
    }

    /**
     * Lista horários em um período específico
     */
    public List<HorarioDisponivel> listarHorariosPorPeriodo(LocalDate dataInicio, LocalDate dataFim) {
        return horarioDAO.listarPorPeriodo(dataInicio, dataFim);
    }

    /**
     * Verifica se um horário está disponível
     */
    public boolean isHorarioDisponivel(int prestadorId, LocalDate data, LocalTime hora) {
        List<HorarioDisponivel> horarios = horarioDAO.listarPorPrestadorEData(prestadorId, data);

        return horarios.stream()
                .anyMatch(h -> h.isDisponivel() &&
                        !h.getHoraInicio().isAfter(hora) &&
                        h.getHoraFim().isAfter(hora) &&
                        !h.isHorarioPassado());
    }

    /**
     * Busca horário específico que contenha um determinado horário
     */
    public HorarioDisponivel buscarHorarioContenhaHora(int prestadorId, LocalDate data, LocalTime hora) {
        List<HorarioDisponivel> horarios = horarioDAO.listarPorPrestadorEData(prestadorId, data);

        return horarios.stream()
                .filter(h -> h.isDisponivel() &&
                        !h.getHoraInicio().isAfter(hora) &&
                        h.getHoraFim().isAfter(hora) &&
                        !h.isHorarioPassado())
                .findFirst()
                .orElse(null);
    }

    /**
     * Remove todos os horários de um prestador
     */
    public boolean removerTodosHorariosPrestador(int prestadorId) {
        try {
            return horarioDAO.removerPorPrestador(prestadorId);
        } catch (Exception e) {
            System.err.println("Erro ao remover horários do prestador: " + e.getMessage());
            return false;
        }
    }

    /**
     * Limpa horários antigos (anteriores à data atual)
     */
    public boolean limparHorariosAntigos() {
        try {
            return horarioDAO.limparHorariosAntigos();
        } catch (Exception e) {
            System.err.println("Erro ao limpar horários antigos: " + e.getMessage());
            return false;
        }
    }

    /**
     * Lista todos os horários
     */
    public List<HorarioDisponivel> listarTodosHorarios() {
        return horarioDAO.listarTodos();
    }

    /**
     * Busca um horário por ID
     */
    public HorarioDisponivel buscarPorId(int id) {
        return horarioDAO.buscarPorId(id);
    }

    /**
     * Lista todos os horários de um prestador
     */
    public List<HorarioDisponivel> listarHorariosPorPrestador(int prestadorId) {
        return horarioDAO.listarPorPrestador(prestadorId);
    }
}
