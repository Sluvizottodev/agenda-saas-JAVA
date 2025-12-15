package br.cefet.agendasaas.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.cefet.agendasaas.dto.JwtResponse;
import br.cefet.agendasaas.dto.LoginRequest;
import br.cefet.agendasaas.dto.UsuarioAuthResponse;
import br.cefet.agendasaas.model.entidades.Cliente;
import br.cefet.agendasaas.model.entidades.Prestador;
import br.cefet.agendasaas.model.entidades.Usuario;
import br.cefet.agendasaas.service.AuthService;
import br.cefet.agendasaas.service.CadastroService;
import br.cefet.agendasaas.utils.ValidationException;
import jakarta.validation.Valid;

/**
 * Controller para autenticação e autorização
 * 
 * Endpoints:
 * - POST /login: Autentica usuário (CLIENTE ou PRESTADOR)
 * - POST /register/cliente: Registra novo CLIENTE
 * - POST /register/prestador: Registra novo PRESTADOR
 * - GET /me: Retorna informações do usuário autenticado
 * - POST /logout: Realiza logout
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private CadastroService cadastroService;

    /**
     * Login de usuário (CLIENTE ou PRESTADOR)
     */
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            JwtResponse jwtResponse = authService.authenticateUser(loginRequest);
            return ResponseEntity.ok(jwtResponse);
        } catch (ValidationException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Erro interno do servidor");
            return ResponseEntity.status(500).body(error);
        }
    }

    /**
     * Registro de novo CLIENTE
     */
    @PostMapping("/register/cliente")
    public ResponseEntity<?> registerCliente(@Valid @RequestBody Cliente cliente) {
        try {
            if (authService.userExists(cliente.getEmail())) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Email já cadastrado");
                return ResponseEntity.badRequest().body(error);
            }

            cadastroService.cadastrarCliente(cliente);

            LoginRequest loginRequest = new LoginRequest(cliente.getEmail(), cliente.getSenha());
            JwtResponse jwtResponse = authService.authenticateUser(loginRequest);

            return ResponseEntity.ok(jwtResponse);
        } catch (ValidationException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Erro interno do servidor");
            return ResponseEntity.status(500).body(error);
        }
    }

    /**
     * Registro de novo PRESTADOR
     */
    @PostMapping("/register/prestador")
    public ResponseEntity<?> registerPrestador(@Valid @RequestBody Prestador prestador) {
        try {
            if (authService.userExists(prestador.getEmail())) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Email já cadastrado");
                return ResponseEntity.badRequest().body(error);
            }

            cadastroService.cadastrarPrestador(prestador);

            LoginRequest loginRequest = new LoginRequest(prestador.getEmail(), prestador.getSenha());
            JwtResponse jwtResponse = authService.authenticateUser(loginRequest);

            return ResponseEntity.ok(jwtResponse);
        } catch (ValidationException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Erro interno do servidor");
            return ResponseEntity.status(500).body(error);
        }
    }

    /**
     * Retorna informações do usuário autenticado
     * Requer token JWT válido
     */
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        try {
            if (authentication == null) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Não autenticado");
                return ResponseEntity.status(401).body(error);
            }

            String email = authentication.getName();
            Usuario user = authService.findUserByEmail(email);

            // Determina o role baseado no tipo de usuário
            String role = user instanceof Cliente ? "CLIENTE" : "PRESTADOR";

            UsuarioAuthResponse response = new UsuarioAuthResponse(
                    user.getId(),
                    user.getNome(),
                    user.getEmail(),
                    role);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Erro ao buscar usuário");
            return ResponseEntity.status(500).body(error);
        }
    }

    /**
     * Logout do usuário
     * Operação no lado do cliente (remover token)
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser() {
        Map<String, String> message = new HashMap<>();
        message.put("message", "Logout realizado com sucesso");
        return ResponseEntity.ok(message);
    }
}