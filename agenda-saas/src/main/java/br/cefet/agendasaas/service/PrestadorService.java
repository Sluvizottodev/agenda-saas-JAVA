package br.cefet.agendasaas.service;

import java.util.List;

import br.cefet.agendasaas.dao.GenericDAO;
import br.cefet.agendasaas.model.entidades.Prestador;

public class PrestadorService {

    private final GenericDAO<Prestador, Integer> dao = new GenericDAO<>(Prestador.class);

    public PrestadorService() {
        // construtor padrão
    }

    public Prestador criar(Prestador p) {
        return dao.save(p);
    }

    public Prestador buscarPorId(int id) {
        return dao.findById(id).orElse(null);
    }

    public List<Prestador> listarTodos() {
        return dao.findAll();
    }

    public Prestador atualizar(Prestador p) {
        return dao.update(p);
    }

    public boolean remover(int id) {
        return dao.deleteById(id) > 0;
    }
}
