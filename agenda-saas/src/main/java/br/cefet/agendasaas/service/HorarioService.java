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

    public HorarioService(HorarioDisponivelDAO horarioDAO) {
        this.horarioDAO = horarioDAO;
    }

    public boolean cadastrarHorario(HorarioDisponivel horario) {
        try {
            if (horario.getData().isBefore(LocalDate.now())) {
                throw new IllegalArgumentException("Não é possível cadastrar horários no passado");
            }

            if (horario.getHoraInicio().isAfter(horario.getHoraFim())) {
                throw new IllegalArgumentException("Hora de início deve ser anterior à hora de fim");
            }
            if (horarioDAO.verificarConflito(horario.getPrestadorId(), horario.getData(),
                    horario.getHoraInicio(), horario.getHoraFim())) {
                throw new IllegalArgumentException("Já existe um horário conflitante cadastrado");
            }

            return horarioDAO.inserir(horario);

        } catch (Exception e) {
            System.err.println("Erro ao cadastrar horário (DB): " + e.getMessage());
            return false;
        }
    }

    public List<HorarioDisponivel> buscarHorariosDisponiveisPorPrestador(int prestadorId) {
        return horarioDAO.listarDisponiveisPorPrestador(prestadorId);
    }

    public List<HorarioDisponivel> buscarHorariosPorPrestadorEData(int prestadorId, LocalDate data) {
        return horarioDAO.listarPorPrestadorEData(prestadorId, data);
    }

    public List<HorarioDisponivel> buscarHorariosDisponiveisPorPrestadorEData(int prestadorId, LocalDate data) {
        return horarioDAO.listarPorPrestadorEData(prestadorId, data)
                .stream()
                .filter(HorarioDisponivel::isDisponivel)
                .filter(horario -> !horario.isHorarioPassado())
                .collect(Collectors.toList());
    }

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

        } catch (jakarta.persistence.PersistenceException e) {
            System.err.println("Erro ao reservar horário (DB): " + e.getMessage());
            return false;
        }
    }

    public boolean liberarHorario(int horarioId) {
        try {
            HorarioDisponivel horario = horarioDAO.buscarPorId(horarioId);

            if (horario == null) {
                throw new IllegalArgumentException("HorÃ¡rio nÃ£o encontrado");
            }

            return horarioDAO.marcarComoDisponivel(horarioId);

        } catch (jakarta.persistence.PersistenceException e) {
            System.err.println("Erro ao liberar horário (DB): " + e.getMessage());
            return false;
        }
    }

    public boolean atualizarHorario(HorarioDisponivel horario) {
        try {

            if (horario.getId() <= 0) {
                throw new IllegalArgumentException("ID do horário é obrigatório para atualização");
            }

            HorarioDisponivel horarioExistente = horarioDAO.buscarPorId(horario.getId());
            if (horarioExistente == null) {
                throw new IllegalArgumentException("HorÃ¡rio nÃ£o encontrado");
            }

            if (horario.getHoraInicio().isAfter(horario.getHoraFim())) {
                throw new IllegalArgumentException("Hora de inÃ­cio deve ser anterior Ã  hora de fim");
            }

            return horarioDAO.atualizar(horario);

        } catch (jakarta.persistence.PersistenceException e) {
            System.err.println("Erro ao atualizar horário (DB): " + e.getMessage());
            return false;
        }
    }

    public boolean removerHorario(int horarioId) {
        try {
            HorarioDisponivel horario = horarioDAO.buscarPorId(horarioId);

            if (horario == null) {
                throw new IllegalArgumentException("HorÃ¡rio nÃ£o encontrado");
            }

            return horarioDAO.remover(horarioId);

        } catch (jakarta.persistence.PersistenceException e) {
            System.err.println("Erro ao remover horário (DB): " + e.getMessage());
            return false;
        }
    }

    public boolean gerarHorariosAutomaticos(int prestadorId, LocalDate dataInicio, LocalDate dataFim,
            LocalTime horaInicioTrabalho, LocalTime horaFimTrabalho,
            int intervalosMinutos) {
        try {

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

        } catch (jakarta.persistence.PersistenceException e) {
            System.err.println("Erro ao gerar horários automáticos (DB): " + e.getMessage());
            return false;
        }
    }

    public List<HorarioDisponivel> listarHorariosPorPeriodo(LocalDate dataInicio, LocalDate dataFim) {
        return horarioDAO.listarPorPeriodo(dataInicio, dataFim);
    }

    public boolean isHorarioDisponivel(int prestadorId, LocalDate data, LocalTime hora) {
        List<HorarioDisponivel> horarios = horarioDAO.listarPorPrestadorEData(prestadorId, data);

        return horarios.stream()
                .anyMatch(h -> h.isDisponivel() &&
                        !h.getHoraInicio().isAfter(hora) &&
                        h.getHoraFim().isAfter(hora) &&
                        !h.isHorarioPassado());
    }

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

    public boolean removerTodosHorariosPrestador(int prestadorId) {
        try {
            return horarioDAO.removerPorPrestador(prestadorId);
        } catch (jakarta.persistence.PersistenceException e) {
            System.err.println("Erro ao remover horários do prestador (DB): " + e.getMessage());
            return false;
        }
    }

    public boolean limparHorariosAntigos() {
        try {
            return horarioDAO.limparHorariosAntigos();
        } catch (jakarta.persistence.PersistenceException e) {
            System.err.println("Erro ao limpar horários antigos (DB): " + e.getMessage());
            return false;
        }
    }

    public List<HorarioDisponivel> listarTodosHorarios() {
        return horarioDAO.listarTodos();
    }

    public HorarioDisponivel buscarPorId(int id) {
        return horarioDAO.buscarPorId(id);
    }

    public List<HorarioDisponivel> listarHorariosPorPrestador(int prestadorId) {
        return horarioDAO.listarPorPrestador(prestadorId);
    }
}
