package br.cefet.agendasaas.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.cefet.agendasaas.dto.JwtResponse;
import br.cefet.agendasaas.dto.LoginRequest;
import br.cefet.agendasaas.model.entidades.Cliente;
import br.cefet.agendasaas.model.entidades.Prestador;
import br.cefet.agendasaas.model.entidades.Usuario;
import br.cefet.agendasaas.repository.ClienteRepository;
import br.cefet.agendasaas.repository.PrestadorRepository;
import br.cefet.agendasaas.security.JwtUtils;
import br.cefet.agendasaas.utils.ValidationException;

@Service
public class AuthService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private PrestadorRepository prestadorRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    public JwtResponse authenticateUser(LoginRequest loginRequest) {
        String email = loginRequest.getEmail();
        String senha = loginRequest.getSenha();

        Optional<Cliente> clienteOpt = clienteRepository.findByEmail(email);
        if (clienteOpt.isPresent()) {
            Cliente cliente = clienteOpt.get();
            if (passwordEncoder.matches(senha, cliente.getSenha())) {
                String jwt = jwtUtils.generateJwtToken(email, "CLIENTE");
                return new JwtResponse(jwt, cliente.getEmail(), cliente.getNome(), "CLIENTE", cliente.getId());
            }
        }

        Optional<Prestador> prestadorOpt = prestadorRepository.findByEmail(email);
        if (prestadorOpt.isPresent()) {
            Prestador prestador = prestadorOpt.get();
            if (passwordEncoder.matches(senha, prestador.getSenha())) {
                String jwt = jwtUtils.generateJwtToken(email, "PRESTADOR");
                return new JwtResponse(jwt, prestador.getEmail(), prestador.getNome(), "PRESTADOR", prestador.getId());
            }
        }

        throw new ValidationException("Credenciais inv\u00e1lidas");
    }

    public boolean userExists(String email) {
        return clienteRepository.findByEmail(email).isPresent() ||
                prestadorRepository.findByEmail(email).isPresent();
    }

    public Usuario findUserByEmail(String email) {
        Optional<Cliente> cliente = clienteRepository.findByEmail(email);
        if (cliente.isPresent()) {
            return cliente.get();
        }

        Optional<Prestador> prestador = prestadorRepository.findByEmail(email);
        if (prestador.isPresent()) {
            return prestador.get();
        }

        throw new ValidationException("Usu\u00e1rio n\u00e3o encontrado");
    }
}