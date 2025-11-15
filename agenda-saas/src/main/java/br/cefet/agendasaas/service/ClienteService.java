package br.cefet.agendasaas.service;

import org.springframework.stereotype.Service;

import br.cefet.agendasaas.dao.ClienteDAO;
import br.cefet.agendasaas.model.entidades.Cliente;
import br.cefet.agendasaas.utils.ValidationException;

@Service
public class ClienteService {

    private final ClienteDAO clienteDAO;

    public ClienteService(ClienteDAO clienteDAO) {
        this.clienteDAO = clienteDAO;
    }

    public void cadastrarCliente(Cliente cliente) throws ValidationException {
        validarCliente(cliente);

        boolean sucesso = clienteDAO.inserir(cliente);
        if (!sucesso) {
            throw new ValidationException("Erro ao cadastrar cliente no banco de dados.");
        }
    }

    public Cliente buscarClientePorId(int id) throws ValidationException {
        if (id <= 0) {
            throw new ValidationException("ID do cliente deve ser positivo.");
        }

        Cliente cliente = clienteDAO.buscarPorId(id);
        if (cliente == null) {
            throw new ValidationException("Cliente não encontrado.");
        }
        return cliente;
    }

    public void atualizarCliente(Cliente cliente) throws ValidationException {
        if (cliente.getId() <= 0) {
            throw new ValidationException("ID do cliente é obrigatório para atualização.");
        }

        validarCliente(cliente);

        boolean sucesso = clienteDAO.atualizar(cliente);
        if (!sucesso) {
            throw new ValidationException("Erro ao atualizar cliente no banco de dados.");
        }
    }

    public void removerCliente(int id) throws ValidationException {
        if (id <= 0) {
            throw new ValidationException("ID do cliente deve ser positivo.");
        }

        Cliente cliente = clienteDAO.buscarPorId(id);
        if (cliente == null) {
            throw new ValidationException("Cliente não encontrado para remoção.");
        }

        boolean sucesso = clienteDAO.remover(id);
        if (!sucesso) {
            throw new ValidationException("Erro ao remover cliente do banco de dados.");
        }
    }

    private void validarCliente(Cliente cliente) throws ValidationException {
        if (cliente == null) {
            throw new ValidationException("Cliente não pode ser nulo.");
        }
        if (cliente.getNome() == null || cliente.getNome().trim().isEmpty()) {
            throw new ValidationException("O nome é obrigatório.");
        }
        if (cliente.getEmail() == null || cliente.getEmail().trim().isEmpty()) {
            throw new ValidationException("O email é obrigatório.");
        }
        if (cliente.getSenha() == null || cliente.getSenha().trim().isEmpty()) {
            throw new ValidationException("A senha é obrigatória.");
        }
        if (cliente.getCpf() == null || cliente.getCpf().trim().isEmpty()) {
            throw new ValidationException("O CPF é obrigatório.");
        }
    }
}
