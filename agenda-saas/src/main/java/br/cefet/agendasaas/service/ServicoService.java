package br.cefet.agendasaas.service;

import java.util.List;

import br.cefet.agendasaas.dao.ServicoDAO;
import br.cefet.agendasaas.model.entidades.Servico;

public class ServicoService {

    private final ServicoDAO dao;

    public ServicoService() {
        this.dao = new ServicoDAO();
    }

    public ServicoService(ServicoDAO dao) {
        this.dao = dao;
    }

    public boolean cadastrar(Servico s) {
        return dao.inserir(s);
    }

    public Servico buscarPorId(int id) {
        return dao.buscarPorId(id);
    }

    public List<Servico> listarTodos() {
        return dao.listarTodos();
    }

    public boolean atualizar(Servico s) {
        return dao.atualizar(s);
    }

    public boolean remover(int id) {
        return dao.remover(id);
    }

    public List<Servico> listarPorPrestador(int prestadorId) {
        return dao.listarPorPrestador(prestadorId);
    }
}
