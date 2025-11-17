package br.cefet.agendasaas.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.cefet.agendasaas.model.entidades.HorarioDisponivel;
import br.cefet.agendasaas.repository.HorarioDisponivelRepository;
import br.cefet.agendasaas.utils.ValidationException;

@Service
public class HorarioService {

    @Autowired
    private HorarioDisponivelRepository horarioDisponivelRepository;

    public HorarioDisponivel salvar(HorarioDisponivel horario) throws ValidationException {
        validarHorario(horario);

        List<HorarioDisponivel> horariosExistentes = horarioDisponivelRepository
                .findByPrestadorIdAndData(horario.getPrestadorId(), horario.getData());
        for (HorarioDisponivel existente : horariosExistentes) {
            if (existente.isHorarioConflitante(horario.getHoraInicio(), horario.getHoraFim())) {
                throw new ValidationException(
                        "Já existe um horário disponível conflitante para este prestador neste período.");
            }
        }

        return horarioDisponivelRepository.save(horario);
    }

    public Optional<HorarioDisponivel> buscarPorId(Integer id) {
        return horarioDisponivelRepository.findById(id);
    }

    public List<HorarioDisponivel> listarTodos() {
        return horarioDisponivelRepository.findAll();
    }

    public List<HorarioDisponivel> buscarPorPrestadorId(Integer prestadorId) {
        return horarioDisponivelRepository.findByPrestadorId(prestadorId);
    }

    public List<HorarioDisponivel> buscarPorData(LocalDate data) {
        return horarioDisponivelRepository.findByData(data);
    }

    public List<HorarioDisponivel> buscarPorPrestadorEData(Integer prestadorId, LocalDate data) {
        return horarioDisponivelRepository.findByPrestadorIdAndData(prestadorId, data);
    }

    public List<HorarioDisponivel> buscarDisponiveis() {
        return horarioDisponivelRepository.findByDisponivel(true);
    }

    public List<HorarioDisponivel> buscarDisponiveisPorPrestador(Integer prestadorId) {
        return horarioDisponivelRepository.findByPrestadorIdAndDisponivel(prestadorId, true);
    }

    public List<HorarioDisponivel> buscarDisponiveisPorData(LocalDate data) {
        return horarioDisponivelRepository.findByDataAndDisponivel(data, true);
    }

    public List<HorarioDisponivel> buscarDisponiveisPorPrestadorEData(Integer prestadorId, LocalDate data) {
        return horarioDisponivelRepository.findByPrestadorIdAndDataAndDisponivel(prestadorId, data, true);
    }

    public HorarioDisponivel atualizar(HorarioDisponivel horario) throws ValidationException {
        validarHorario(horario);

        if (horario.getId() == null) {
            throw new ValidationException("ID do horário não pode ser nulo para atualização.");
        }

        Optional<HorarioDisponivel> horarioExistente = horarioDisponivelRepository.findById(horario.getId());
        if (horarioExistente.isEmpty()) {
            throw new ValidationException("Horário não encontrado para atualização.");
        }

        List<HorarioDisponivel> horariosExistentes = horarioDisponivelRepository
                .findByPrestadorIdAndData(horario.getPrestadorId(), horario.getData());
        for (HorarioDisponivel existente : horariosExistentes) {
            if (!existente.getId().equals(horario.getId()) &&
                    existente.isHorarioConflitante(horario.getHoraInicio(), horario.getHoraFim())) {
                throw new ValidationException(
                        "Já existe um horário disponível conflitante para este prestador neste período.");
            }
        }

        return horarioDisponivelRepository.save(horario);
    }

    public void excluir(Integer id) throws ValidationException {
        if (id == null) {
            throw new ValidationException("ID do horário não pode ser nulo para exclusão.");
        }

        Optional<HorarioDisponivel> horario = horarioDisponivelRepository.findById(id);
        if (horario.isEmpty()) {
            throw new ValidationException("Horário não encontrado para exclusão.");
        }

        horarioDisponivelRepository.deleteById(id);
    }

    public void marcarComoOcupado(Integer id) throws ValidationException {
        Optional<HorarioDisponivel> horarioOpt = horarioDisponivelRepository.findById(id);

        if (horarioOpt.isEmpty()) {
            throw new ValidationException("Horário não encontrado.");
        }

        HorarioDisponivel horario = horarioOpt.get();
        if (!horario.isDisponivel()) {
            throw new ValidationException("Este horário já está ocupado.");
        }

        horario.setDisponivel(false);
        horarioDisponivelRepository.save(horario);
    }

    public void marcarComoDisponivel(Integer id) throws ValidationException {
        Optional<HorarioDisponivel> horarioOpt = horarioDisponivelRepository.findById(id);

        if (horarioOpt.isEmpty()) {
            throw new ValidationException("Horário não encontrado.");
        }

        HorarioDisponivel horario = horarioOpt.get();
        horario.setDisponivel(true);
        horarioDisponivelRepository.save(horario);
    }

    private void validarHorario(HorarioDisponivel horario) throws ValidationException {
        if (horario == null) {
            throw new ValidationException("Horário não pode ser nulo.");
        }

        if (horario.getPrestadorId() == null) {
            throw new ValidationException("Prestador é obrigatório.");
        }

        if (horario.getData() == null) {
            throw new ValidationException("Data é obrigatória.");
        }

        if (horario.getHoraInicio() == null) {
            throw new ValidationException("Hora de início é obrigatória.");
        }

        if (horario.getHoraFim() == null) {
            throw new ValidationException("Hora de fim é obrigatória.");
        }

        if (horario.getData().isBefore(LocalDate.now())) {
            throw new ValidationException("Não é possível criar horários para datas passadas.");
        }

        LocalTime horarioInicio = LocalTime.of(8, 0);
        LocalTime horarioFim = LocalTime.of(18, 0);

        if (horario.getHoraInicio().isBefore(horarioInicio) || horario.getHoraFim().isAfter(horarioFim)) {
            throw new ValidationException("Horário deve estar entre 08:00 e 18:00.");
        }

        if (horario.getHoraFim().isBefore(horario.getHoraInicio()) ||
                horario.getHoraFim().equals(horario.getHoraInicio())) {
            throw new ValidationException("Hora de fim deve ser posterior à hora de início.");
        }
    }
}
