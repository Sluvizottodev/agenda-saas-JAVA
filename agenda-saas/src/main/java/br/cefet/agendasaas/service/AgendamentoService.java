package br.cefet.agendasaas.service;

import java.util.List;

import br.cefet.agendasaas.dao.AgendamentoDAO;
import br.cefet.agendasaas.model.entidades.Agendamento;

public class AgendamentoService {

    private final AgendamentoDAO dao;

    public AgendamentoService() {
        this.dao = new AgendamentoDAO();
    }

    public boolean agendar(Agendamento a) {
        return dao.inserir(a);
    }

    public Agendamento buscarPorId(int id) {
        return dao.buscarPorId(id);
    }

    public boolean atualizar(Agendamento a) {
        return dao.atualizar(a);
    }

    public boolean cancelar(int id) {
        return dao.remover(id);
    }

    public List<Agendamento> listarPorPrestador(int prestadorId) {
        return dao.listarPorPrestador(prestadorId);
    }

    public List<Agendamento> listarPorCliente(int clienteId) {
        return dao.listarPorCliente(clienteId);
    }
}
