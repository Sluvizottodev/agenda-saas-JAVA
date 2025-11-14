package br.cefet.agendasaas.service;

import br.cefet.agendasaas.dao.ClienteDAO;
import br.cefet.agendasaas.model.entidades.Cliente;
import br.cefet.agendasaas.utils.ValidationException;
import org.springframework.stereotype.Service;

@Service
public class ClienteService {

    private final ClienteDAO clienteDAO;

    public ClienteService(ClienteDAO clienteDAO) {
        this.clienteDAO = clienteDAO;
    }

    public void cadastrarCliente(Cliente cliente) throws ValidationException {
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

        boolean sucesso = clienteDAO.inserir(cliente);
        if (!sucesso) {
            throw new ValidationException("Erro ao cadastrar cliente no banco de dados.");
        }
    }
}
