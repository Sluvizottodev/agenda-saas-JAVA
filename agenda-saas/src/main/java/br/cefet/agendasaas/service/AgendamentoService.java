package br.cefet.agendasaas.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import br.cefet.agendasaas.dao.AgendamentoDAO;
import br.cefet.agendasaas.dao.ServicoDAO;
import br.cefet.agendasaas.model.entidades.Agendamento;
import br.cefet.agendasaas.model.entidades.Servico;
import br.cefet.agendasaas.utils.ValidationException;

@Service
public class AgendamentoService {

    private final AgendamentoDAO agendamentoDAO;
    private final ServicoDAO servicoDAO;

    public AgendamentoService(AgendamentoDAO agendamentoDAO, ServicoDAO servicoDAO) {
        this.agendamentoDAO = agendamentoDAO;
        this.servicoDAO = servicoDAO;
    }

    public List<Agendamento> listarAgendamentos(Integer usuarioId, String tipoUsuario) throws ValidationException {
        if (usuarioId == null) {
            throw new ValidationException("ID do usuário é obrigatório");
        }
        if (tipoUsuario == null || (!"cliente".equalsIgnoreCase(tipoUsuario) && !"prestador".equalsIgnoreCase(tipoUsuario))) {
            throw new ValidationException("Tipo de usuário deve ser 'cliente' ou 'prestador'");
        }
        
        if ("cliente".equalsIgnoreCase(tipoUsuario)) {
            return agendamentoDAO.listarPorCliente(usuarioId);
        } else {
            return agendamentoDAO.listarPorPrestador(usuarioId);
        }
    }

    public void criarAgendamento(Agendamento agendamento) throws ValidationException {
        validarAgendamento(agendamento);
        
        if (agendamento.getDataHora().isBefore(LocalDateTime.now())) {
            throw new ValidationException("A data e horário devem ser futuros");
        }

        Servico servico = servicoDAO.buscarPorId(agendamento.getServicoId());
        if (servico == null) {
            throw new ValidationException("Serviço não encontrado");
        }

        agendamento.setPrestadorId(servico.getPrestadorId());
        agendamento.setStatus("PENDENTE");

        boolean sucesso = agendamentoDAO.inserir(agendamento);
        if (!sucesso) {
            throw new ValidationException("Erro ao salvar o agendamento no banco de dados.");
        }
    }

    public void atualizarAgendamento(Agendamento agendamento) throws ValidationException {
        validarAgendamento(agendamento);
        
        // Verificar se o agendamento existe antes de atualizar
        Agendamento existente = agendamentoDAO.buscarPorId(agendamento.getId());
        if (existente == null) {
            throw new ValidationException("Agendamento não encontrado para atualização");
        }
        
        if (agendamento.getDataHora().isBefore(LocalDateTime.now())) {
            throw new ValidationException("A data e horário devem ser futuros");
        }

        boolean sucesso = agendamentoDAO.atualizar(agendamento);
        if (!sucesso) {
            throw new ValidationException("Erro ao atualizar o agendamento no banco de dados.");
        }
    }

    public void removerAgendamento(int id) throws ValidationException {
        // Verificar se o agendamento existe antes de remover
        Agendamento agendamento = agendamentoDAO.buscarPorId(id);
        if (agendamento == null) {
            throw new ValidationException("Agendamento não encontrado para remoção");
        }
        
        boolean sucesso = agendamentoDAO.remover(id);
        if (!sucesso) {
            throw new ValidationException("Erro ao remover o agendamento no banco de dados.");
        }
    }

    public List<Servico> listarServicosDisponiveis() {
        return servicoDAO.listarTodos();
    }

    public Agendamento buscarAgendamentoPorId(int id) throws ValidationException {
        Agendamento agendamento = agendamentoDAO.buscarPorId(id);
        if (agendamento == null) {
            throw new ValidationException("Agendamento não encontrado.");
        }
        return agendamento;
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