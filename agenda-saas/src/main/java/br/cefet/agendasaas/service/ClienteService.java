
package br.cefet.agendasaas.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import br.cefet.agendasaas.model.entidades.Cliente;
import br.cefet.agendasaas.repository.ClienteRepository;
import br.cefet.agendasaas.utils.ValidationException;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public Cliente cadastrarCliente(Cliente cliente) throws ValidationException {
        validarCliente(cliente);
        return clienteRepository.save(cliente);
    }

    public Cliente buscarClientePorId(int id) throws ValidationException {
        if (id <= 0) {
            throw new ValidationException("ID do cliente deve ser positivo.");
        }

        Optional<Cliente> cliente = clienteRepository.findById(id);
        if (!cliente.isPresent()) {
            throw new ValidationException("Cliente não encontrado.");
        }
        return cliente.get();
    }

    public Cliente atualizarCliente(Cliente cliente) throws ValidationException {
        if (cliente.getId() == null || cliente.getId() <= 0) {
            throw new ValidationException("ID do cliente é obrigatório para atualização.");
        }

        validarCliente(cliente);

        Optional<Cliente> existente = clienteRepository.findById(cliente.getId());
        if (!existente.isPresent()) {
            throw new ValidationException("Cliente não encontrado para atualização.");
        }

        return clienteRepository.save(cliente);
    }

    public void removerCliente(int id) throws ValidationException {
        if (id <= 0) {
            throw new ValidationException("ID do cliente deve ser positivo.");
        }

        Optional<Cliente> cliente = clienteRepository.findById(id);
        if (!cliente.isPresent()) {
            throw new ValidationException("Cliente não encontrado para remoção.");
        }

        clienteRepository.deleteById(id);
    }

    public List<Cliente> listarTodosClientes() {
        return clienteRepository.findAll();
    }

    public Cliente buscarClientePorEmail(String email) throws ValidationException {
        if (email == null || email.trim().isEmpty()) {
            throw new ValidationException("Email é obrigatório.");
        }

        Optional<Cliente> cliente = clienteRepository.findByEmail(email);
        if (!cliente.isPresent()) {
            throw new ValidationException("Cliente não encontrado com o email informado.");
        }
        return cliente.get();
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
