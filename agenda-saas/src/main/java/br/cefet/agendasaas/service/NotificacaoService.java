package br.cefet.agendasaas.service;

import java.util.List;

import br.cefet.agendasaas.dao.GenericDAO;
import br.cefet.agendasaas.model.entidades.Notificacao;

public class NotificacaoService {

    private final GenericDAO<Notificacao, Integer> dao;

    public NotificacaoService() {
        this.dao = new GenericDAO<>(Notificacao.class);
    }

    public NotificacaoService(GenericDAO<Notificacao, Integer> dao) {
        this.dao = dao;
    }

    public Notificacao criar(Notificacao n) {
        return dao.save(n);
    }

    public Notificacao buscarPorId(int id) {
        return dao.findById(id).orElse(null);
    }

    public List<Notificacao> listarTodos() {
        return dao.findAll();
    }

    public Notificacao atualizar(Notificacao n) {
        return dao.update(n);
    }

    public boolean remover(int id) {
        return dao.deleteById(id) > 0;
    }
}
