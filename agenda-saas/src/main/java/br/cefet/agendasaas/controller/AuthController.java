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
import br.cefet.agendasaas.model.entidades.Cliente;
import br.cefet.agendasaas.model.entidades.Prestador;
import br.cefet.agendasaas.service.AuthService;
import br.cefet.agendasaas.service.CadastroService;
import br.cefet.agendasaas.utils.ValidationException;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private CadastroService cadastroService;

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

    @PostMapping("/register/cliente")
    public ResponseEntity<?> registerCliente(@Valid @RequestBody Cliente cliente) {
        try {
            if (authService.userExists(cliente.getEmail())) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Email j\u00e1 cadastrado");
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

    @PostMapping("/register/prestador")
    public ResponseEntity<?> registerPrestador(@Valid @RequestBody Prestador prestador) {
        try {
            if (authService.userExists(prestador.getEmail())) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Email j\u00e1 cadastrado");
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

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        try {
            if (authentication == null) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "N\u00e3o autenticado");
                return ResponseEntity.status(401).body(error);
            }

            String email = authentication.getName();
            var user = authService.findUserByEmail(email);

            Map<String, Object> response = new HashMap<>();
            response.put("id", user.getId());
            response.put("nome", user.getNome());
            response.put("email", user.getEmail());
            response.put("tipoUsuario", user instanceof Cliente ? "CLIENTE" : "PRESTADOR");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Erro ao buscar usu\u00e1rio");
            return ResponseEntity.status(500).body(error);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser() {
        Map<String, String> message = new HashMap<>();
        message.put("message", "Logout realizado com sucesso");
        return ResponseEntity.ok(message);
    }
}