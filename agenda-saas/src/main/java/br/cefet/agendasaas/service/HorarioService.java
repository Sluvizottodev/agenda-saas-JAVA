package br.cefet.agendasaas.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import br.cefet.agendasaas.dao.HorarioDisponivelDAO;
import br.cefet.agendasaas.model.entidades.HorarioDisponivel;

public class HorarioService {

    private final HorarioDisponivelDAO horarioDAO;

    public HorarioService() {
        this.horarioDAO = new HorarioDisponivelDAO();
    }

    /**
     * Cadastra um novo horÃ¡rio disponÃ­vel
     */
    public boolean cadastrarHorario(HorarioDisponivel horario) {
        try {
            // ValidaÃ§Ãµes de negÃ³cio
            if (horario.getData().isBefore(LocalDate.now())) {
                throw new IllegalArgumentException("NÃ£o Ã© possÃ­vel cadastrar horÃ¡rios no passado");
            }

            if (horario.getHoraInicio().isAfter(horario.getHoraFim())) {
                throw new IllegalArgumentException("Hora de inÃ­cio deve ser anterior Ã  hora de fim");
            }

            // Verifica se jÃ¡ existe conflito de horÃ¡rio
            if (horarioDAO.verificarConflito(horario.getPrestadorId(), horario.getData(),
                    horario.getHoraInicio(), horario.getHoraFim())) {
                throw new IllegalArgumentException("JÃ¡ existe um horÃ¡rio conflitante cadastrado");
            }

            return horarioDAO.inserir(horario);

        } catch (Exception e) {
            System.err.println("Erro ao cadastrar horÃ¡rio: " + e.getMessage());
            return false;
        }
    }

    /**
     * Busca horÃ¡rios disponÃ­veis de um prestador
     */
    public List<HorarioDisponivel> buscarHorariosDisponiveisPorPrestador(int prestadorId) {
        return horarioDAO.listarDisponiveisPorPrestador(prestadorId);
    }

    /**
     * Busca horÃ¡rios de um prestador em uma data especÃ­fica
     */
    public List<HorarioDisponivel> buscarHorariosPorPrestadorEData(int prestadorId, LocalDate data) {
        return horarioDAO.listarPorPrestadorEData(prestadorId, data);
    }

    /**
     * Busca apenas horÃ¡rios disponÃ­veis (nÃ£o ocupados) de um prestador em uma data
     * especÃ­fica
     */
    public List<HorarioDisponivel> buscarHorariosDisponiveisPorPrestadorEData(int prestadorId, LocalDate data) {
        return horarioDAO.listarPorPrestadorEData(prestadorId, data)
                .stream()
                .filter(HorarioDisponivel::isDisponivel)
                .filter(horario -> !horario.isHorarioPassado())
                .collect(Collectors.toList());
    }

    /**
     * Reserva um horÃ¡rio (marca como indisponÃ­vel)
     */
    public boolean reservarHorario(int horarioId) {
        try {
            HorarioDisponivel horario = horarioDAO.buscarPorId(horarioId);

            if (horario == null) {
                throw new IllegalArgumentException("HorÃ¡rio nÃ£o encontrado");
            }

            if (!horario.isDisponivel()) {
                throw new IllegalArgumentException("HorÃ¡rio jÃ¡ estÃ¡ ocupado");
            }

            if (horario.isHorarioPassado()) {
                throw new IllegalArgumentException("NÃ£o Ã© possÃ­vel reservar horÃ¡rios no passado");
            }

            return horarioDAO.marcarComoIndisponivel(horarioId);

        } catch (Exception e) {
            System.err.println("Erro ao reservar horÃ¡rio: " + e.getMessage());
            return false;
        }
    }

    /**
     * Libera um horÃ¡rio (marca como disponÃ­vel)
     */
    public boolean liberarHorario(int horarioId) {
        try {
            HorarioDisponivel horario = horarioDAO.buscarPorId(horarioId);

            if (horario == null) {
                throw new IllegalArgumentException("HorÃ¡rio nÃ£o encontrado");
            }

            return horarioDAO.marcarComoDisponivel(horarioId);

        } catch (Exception e) {
            System.err.println("Erro ao liberar horÃ¡rio: " + e.getMessage());
            return false;
        }
    }

    /**
     * Atualiza um horÃ¡rio disponÃ­vel
     */
    public boolean atualizarHorario(HorarioDisponivel horario) {
        try {
            // ValidaÃ§Ãµes de negÃ³cio
            if (horario.getId() <= 0) {
                throw new IllegalArgumentException("ID do horÃ¡rio Ã© obrigatÃ³rio para atualizaÃ§Ã£o");
            }

            HorarioDisponivel horarioExistente = horarioDAO.buscarPorId(horario.getId());
            if (horarioExistente == null) {
                throw new IllegalArgumentException("HorÃ¡rio nÃ£o encontrado");
            }

            if (horario.getHoraInicio().isAfter(horario.getHoraFim())) {
                throw new IllegalArgumentException("Hora de inÃ­cio deve ser anterior Ã  hora de fim");
            }

            return horarioDAO.atualizar(horario);

        } catch (Exception e) {
            System.err.println("Erro ao atualizar horÃ¡rio: " + e.getMessage());
            return false;
        }
    }

    /**
     * Remove um horÃ¡rio
     */
    public boolean removerHorario(int horarioId) {
        try {
            HorarioDisponivel horario = horarioDAO.buscarPorId(horarioId);

            if (horario == null) {
                throw new IllegalArgumentException("HorÃ¡rio nÃ£o encontrado");
            }

            return horarioDAO.remover(horarioId);

        } catch (Exception e) {
            System.err.println("Erro ao remover horÃ¡rio: " + e.getMessage());
            return false;
        }
    }

    /**
     * Gera horÃ¡rios automÃ¡ticos para um prestador
     */
    public boolean gerarHorariosAutomaticos(int prestadorId, LocalDate dataInicio, LocalDate dataFim,
            LocalTime horaInicioTrabalho, LocalTime horaFimTrabalho,
            int intervalosMinutos) {
        try {
            // ValidaÃ§Ãµes
            if (dataInicio.isBefore(LocalDate.now())) {
                throw new IllegalArgumentException("Data de inÃ­cio nÃ£o pode ser no passado");
            }

            if (dataFim.isBefore(dataInicio)) {
                throw new IllegalArgumentException("Data de fim deve ser posterior Ã  data de inÃ­cio");
            }

            if (horaInicioTrabalho.isAfter(horaFimTrabalho)) {
                throw new IllegalArgumentException("Hora de inÃ­cio do trabalho deve ser anterior Ã  hora de fim");
            }

            if (intervalosMinutos <= 0 || intervalosMinutos > 240) {
                throw new IllegalArgumentException("Intervalo deve ser entre 1 e 240 minutos");
            }

            return horarioDAO.gerarHorariosAutomaticos(prestadorId, dataInicio, dataFim,
                    horaInicioTrabalho, horaFimTrabalho,
                    intervalosMinutos);

        } catch (Exception e) {
            System.err.println("Erro ao gerar horÃ¡rios automÃ¡ticos: " + e.getMessage());
            return false;
        }
    }

    /**
     * Lista horÃ¡rios em um perÃ­odo especÃ­fico
     */
    public List<HorarioDisponivel> listarHorariosPorPeriodo(LocalDate dataInicio, LocalDate dataFim) {
        return horarioDAO.listarPorPeriodo(dataInicio, dataFim);
    }

    /**
     * Verifica se um horÃ¡rio estÃ¡ disponÃ­vel
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
     * Busca horÃ¡rio especÃ­fico que contenha um determinado horÃ¡rio
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
     * Remove todos os horÃ¡rios de um prestador
     */
    public boolean removerTodosHorariosPrestador(int prestadorId) {
        try {
            return horarioDAO.removerPorPrestador(prestadorId);
        } catch (Exception e) {
            System.err.println("Erro ao remover horÃ¡rios do prestador: " + e.getMessage());
            return false;
        }
    }

    /**
     * Limpa horÃ¡rios antigos (anteriores Ã  data atual)
     */
    public boolean limparHorariosAntigos() {
        try {
            return horarioDAO.limparHorariosAntigos();
        } catch (Exception e) {
            System.err.println("Erro ao limpar horÃ¡rios antigos: " + e.getMessage());
            return false;
        }
    }

    /**
     * Lista todos os horÃ¡rios
     */
    public List<HorarioDisponivel> listarTodosHorarios() {
        return horarioDAO.listarTodos();
    }

    /**
     * Busca um horÃ¡rio por ID
     */
    public HorarioDisponivel buscarPorId(int id) {
        return horarioDAO.buscarPorId(id);
    }

    /**
     * Lista todos os horÃ¡rios de um prestador
     */
    public List<HorarioDisponivel> listarHorariosPorPrestador(int prestadorId) {
        return horarioDAO.listarPorPrestador(prestadorId);
    }
}

