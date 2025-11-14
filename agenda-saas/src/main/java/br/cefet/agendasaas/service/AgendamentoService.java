package br.cefet.agendasaas.service;

import br.cefet.agendasaas.dao.AgendamentoDAO;
import br.cefet.agendasaas.dao.ServicoDAO;
import br.cefet.agendasaas.model.entidades.Agendamento;
import br.cefet.agendasaas.model.entidades.Servico;
import br.cefet.agendasaas.utils.ValidationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AgendamentoService {

    private final AgendamentoDAO agendamentoDAO;
    private final ServicoDAO servicoDAO;

    public AgendamentoService(AgendamentoDAO agendamentoDAO, ServicoDAO servicoDAO) {
        this.agendamentoDAO = agendamentoDAO;
        this.servicoDAO = servicoDAO;
    }

    public List<Agendamento> listarAgendamentos(Integer usuarioId, String tipoUsuario) {
        if ("cliente".equalsIgnoreCase(tipoUsuario)) {
            return agendamentoDAO.listarPorCliente(usuarioId);
        } else {
            return agendamentoDAO.listarPorPrestador(usuarioId);
        }
    }

    public void criarAgendamento(Agendamento agendamento) throws ValidationException {
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
        if (agendamento.getDataHora().isBefore(LocalDateTime.now())) {
            throw new ValidationException("A data e horário devem ser futuros");
        }

        boolean sucesso = agendamentoDAO.atualizar(agendamento);
        if (!sucesso) {
            throw new ValidationException("Erro ao atualizar o agendamento no banco de dados.");
        }
    }

    public void removerAgendamento(int id) throws ValidationException {
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
}
