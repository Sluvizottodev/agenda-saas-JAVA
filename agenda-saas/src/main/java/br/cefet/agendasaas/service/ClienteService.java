package br.cefet.agendasaas.service;

import java.util.List;

import br.cefet.agendasaas.dao.ClienteDAO;
import br.cefet.agendasaas.model.entidades.Cliente;

public class ClienteService {

    private final ClienteDAO dao;

    public ClienteService() {
        this.dao = new ClienteDAO();
    }

    public boolean cadastrar(Cliente c) {
        return dao.inserir(c);
    }

    public Cliente buscarPorId(int id) {
        return dao.buscarPorId(id);
    }

    public List<Cliente> listarTodos() {
        return dao.listarTodos();
    }

    public boolean atualizar(Cliente c) {
        return dao.atualizar(c);
    }

    public boolean remover(int id) {
        return dao.remover(id);
    }
}
