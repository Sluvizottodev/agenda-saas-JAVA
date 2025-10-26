package br.cefet.agendasaas.service;

import java.util.List;

import br.cefet.agendasaas.dao.GenericDAO;
import br.cefet.agendasaas.model.entidades.Pagamento;

public class PagamentoService {

    private final GenericDAO<Pagamento, Integer> dao = new GenericDAO<>(Pagamento.class);

    public PagamentoService() {
        // construtor padrão
    }

    public Pagamento criar(Pagamento p) {
        return dao.save(p);
    }

    public Pagamento buscarPorId(int id) {
        return dao.findById(id).orElse(null);
    }

    public List<Pagamento> listarTodos() {
        return dao.findAll();
    }

    public Pagamento atualizar(Pagamento p) {
        return dao.update(p);
    }

    public boolean remover(int id) {
        return dao.deleteById(id) > 0;
    }
}
