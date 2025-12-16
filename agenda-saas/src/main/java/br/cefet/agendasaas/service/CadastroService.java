package br.cefet.agendasaas.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.cefet.agendasaas.model.entidades.Cliente;
import br.cefet.agendasaas.model.entidades.Prestador;
import br.cefet.agendasaas.repository.ClienteRepository;
import br.cefet.agendasaas.repository.PrestadorRepository;
import br.cefet.agendasaas.utils.InputValidator;
import br.cefet.agendasaas.utils.ValidationException;

@Service
@Transactional
public class CadastroService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private PrestadorRepository prestadorRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Cliente cadastrarCliente(Cliente cliente) throws ValidationException {
        // Validações
        InputValidator.requireNonBlank(cliente.getNome(), "Nome");
        InputValidator.validateEmail(cliente.getEmail());
        InputValidator.requireNonBlank(cliente.getSenha(), "Senha");
        InputValidator.requireNonBlank(cliente.getCpf(), "CPF");

        if (clienteRepository.existsByEmail(cliente.getEmail())) {
            throw new ValidationException("Email já cadastrado");
        }

        if (clienteRepository.existsByCpf(cliente.getCpf())) {
            throw new ValidationException("CPF já cadastrado");
        }

        cliente.setSenha(passwordEncoder.encode(cliente.getSenha()));

        try {
            return clienteRepository.save(cliente);
        } catch (Exception e) {
            throw new ValidationException("Erro ao salvar cliente: " + e.getMessage());
        }
    }

    public Prestador cadastrarPrestador(Prestador prestador) throws ValidationException {
        InputValidator.requireNonBlank(prestador.getNome(), "Nome");
        InputValidator.validateEmail(prestador.getEmail());
        InputValidator.requireNonBlank(prestador.getSenha(), "Senha");
        InputValidator.requireNonBlank(prestador.getTelefone(), "Telefone");
        InputValidator.requireNonBlank(prestador.getEspecializacao(), "Especialização");
        InputValidator.requireNonBlank(prestador.getCnpj(), "CNPJ");

        if (prestadorRepository.existsByEmail(prestador.getEmail())) {
            throw new ValidationException("Email já cadastrado");
        }

        if (prestadorRepository.existsByCnpj(prestador.getCnpj())) {
            throw new ValidationException("CNPJ já cadastrado");
        }

        prestador.setSenha(passwordEncoder.encode(prestador.getSenha()));

        try {
            return prestadorRepository.save(prestador);
        } catch (Exception e) {
            throw new ValidationException("Erro ao salvar prestador: " + e.getMessage());
        }
    }
}