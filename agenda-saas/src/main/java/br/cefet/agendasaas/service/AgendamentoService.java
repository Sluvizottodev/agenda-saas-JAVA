package br.cefet.agendasaas.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import br.cefet.agendasaas.model.entidades.Agendamento;
import br.cefet.agendasaas.model.entidades.Servico;
import br.cefet.agendasaas.repository.AgendamentoRepository;
import br.cefet.agendasaas.repository.ServicoRepository;
import br.cefet.agendasaas.utils.ValidationException;

@Service
public class AgendamentoService {

    private final AgendamentoRepository agendamentoRepository;
    private final ServicoRepository servicoRepository;

    public AgendamentoService(AgendamentoRepository agendamentoRepository, ServicoRepository servicoRepository) {
        this.agendamentoRepository = agendamentoRepository;
        this.servicoRepository = servicoRepository;
    }

    public List<Agendamento> listarAgendamentos(Integer usuarioId, String tipoUsuario) throws ValidationException {
        if (usuarioId == null) {
            throw new ValidationException("ID do usuário é obrigatório");
        }
        if (tipoUsuario == null || (!"cliente".equalsIgnoreCase(tipoUsuario) && !"prestador".equalsIgnoreCase(tipoUsuario))) {
            throw new ValidationException("Tipo de usuário deve ser 'cliente' ou 'prestador'");
        }
        
        if ("cliente".equalsIgnoreCase(tipoUsuario)) {
            return agendamentoRepository.findByClienteId(usuarioId);
        } else {
            return agendamentoRepository.findByPrestadorId(usuarioId);
        }
    }

    public void criarAgendamento(Agendamento agendamento) throws ValidationException {
        validarAgendamento(agendamento);
        
        if (agendamento.getDataHora().isBefore(LocalDateTime.now())) {
            throw new ValidationException("A data e horário devem ser futuros");
        }

        Integer servicoId = agendamento.getServicoId();
        if (servicoId == null || servicoId <= 0) {
            throw new ValidationException("ID do serviço é obrigatório.");
        }
        Servico servico = servicoRepository.findById(servicoId)
                .orElseThrow(() -> new ValidationException("Serviço não encontrado"));

        agendamento.setPrestadorId(servico.getPrestadorId());
        agendamento.setStatus("PENDENTE");

        agendamentoRepository.save(agendamento);
    }

    public void atualizarAgendamento(Agendamento agendamento) throws ValidationException {
        validarAgendamento(agendamento);
        
        // Verificar se o agendamento existe antes de atualizar
        Integer agendamentoId = agendamento.getId();
        if (agendamentoId == null) {
            throw new ValidationException("ID do agendamento é obrigatório para atualização.");
        }
        agendamentoRepository.findById(agendamentoId)
                .orElseThrow(() -> new ValidationException("Agendamento não encontrado para atualização."));
        
        if (agendamento.getDataHora().isBefore(LocalDateTime.now())) {
            throw new ValidationException("A data e horário devem ser futuros");
        }

        agendamentoRepository.save(agendamento);
    }

    public void removerAgendamento(int id) throws ValidationException {
        // Verificar se o agendamento existe antes de remover
        agendamentoRepository.findById(id)
                .orElseThrow(() -> new ValidationException("Agendamento não encontrado para remoção"));
        
        agendamentoRepository.deleteById(id);
    }

    public List<Servico> listarServicosDisponiveis() {
        return servicoRepository.findAll();
    }

    public Agendamento buscarAgendamentoPorId(int id) throws ValidationException {
        return agendamentoRepository.findById(id)
                .orElseThrow(() -> new ValidationException("Agendamento não encontrado."));
    }
    
    private void validarAgendamento(Agendamento agendamento) throws ValidationException {
        if (agendamento == null) {
            throw new ValidationException("Agendamento não pode ser nulo");
        }
        if (agendamento.getDataHora() == null) {
            throw new ValidationException("Data e hora são obrigatórios");
        }
        if (agendamento.getServicoId() <= 0) {
            throw new ValidationException("ID do serviço é obrigatório");
        }
        if (agendamento.getClienteId() <= 0) {
            throw new ValidationException("ID do cliente é obrigatório");
        }
    }
}