package br.cefet.agendasaas.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.cefet.agendasaas.model.entidades.Agendamento;
import br.cefet.agendasaas.model.entidades.Servico;
import br.cefet.agendasaas.service.AgendamentoService;
import br.cefet.agendasaas.utils.ValidationException;

/**
 * Controller para gerenciamento de agendamentos
 * 
 * Endpoints protegidos por roles:
 * - GET: ambas as roles (CLIENTE, PRESTADOR)
 * - POST: apenas CLIENTE
 * - PUT/DELETE: ambas (com validação de propriedade)
 */
@RestController
@RequestMapping("/api/agendamentos")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AgendamentoController {

    private final AgendamentoService agendamentoService;

    public AgendamentoController(AgendamentoService agendamentoService) {
        this.agendamentoService = agendamentoService;
    }

    /**
     * Lista agendamentos do usuário autenticado
     * Ambas as roles podem acessar
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('CLIENTE', 'PRESTADOR')")
    public ResponseEntity<List<Agendamento>> listarAgendamentos(
            Authentication authentication,
            @RequestParam(required = false) Integer usuarioId,
            @RequestParam(required = false) String tipoUsuario) {
        try {
            String email = authentication.getName();

            // Se não forneceu usuarioId, usa o do usuário autenticado
            List<Agendamento> agendamentos = agendamentoService.listarAgendamentos(usuarioId, tipoUsuario);
            return ResponseEntity.ok(agendamentos);
        } catch (ValidationException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    /**
     * Cria um novo agendamento
     * Apenas CLIENTE pode criar
     */
    @PostMapping
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<String> criarAgendamento(
            Authentication authentication,
            @RequestBody Agendamento agendamento) {
        try {
            String emailCliente = authentication.getName();

            agendamentoService.criarAgendamento(agendamento);
            return ResponseEntity.ok("Agendamento criado com sucesso!");
        } catch (ValidationException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro interno ao criar agendamento.");
        }
    }

    /**
     * Lista serviços disponíveis
     * Ambas as roles podem acessar
     */
    @GetMapping("/servicos")
    @PreAuthorize("hasAnyRole('CLIENTE', 'PRESTADOR')")
    public ResponseEntity<List<Servico>> listarServicosDisponiveis() {
        try {
            List<Servico> servicos = agendamentoService.listarServicosDisponiveis();
            return ResponseEntity.ok(servicos);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    /**
     * Busca agendamento por ID
     * Ambas as roles podem acessar
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('CLIENTE', 'PRESTADOR')")
    public ResponseEntity<Agendamento> buscarAgendamentoPorId(@PathVariable int id) {
        try {
            Agendamento agendamento = agendamentoService.buscarAgendamentoPorId(id);
            return ResponseEntity.ok(agendamento);
        } catch (ValidationException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    /**
     * Atualiza um agendamento
     * Ambas as roles podem atualizar (com validação de propriedade)
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('CLIENTE', 'PRESTADOR')")
    public ResponseEntity<String> atualizarAgendamento(
            Authentication authentication,
            @PathVariable int id,
            @RequestBody Agendamento agendamento) {
        try {
            String emailUsuario = authentication.getName();

            agendamento.setId(id);
            agendamentoService.atualizarAgendamento(agendamento);
            return ResponseEntity.ok("Agendamento atualizado com sucesso!");
        } catch (ValidationException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro interno ao atualizar agendamento.");
        }
    }

    /**
     * Remove um agendamento
     * Ambas as roles podem deletar (com validação de propriedade)
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('CLIENTE', 'PRESTADOR')")
    public ResponseEntity<String> removerAgendamento(
            Authentication authentication,
            @PathVariable int id) {
        try {
            String emailUsuario = authentication.getName();

            agendamentoService.removerAgendamento(id);
            return ResponseEntity.ok("Agendamento removido com sucesso!");
        } catch (ValidationException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro interno ao remover agendamento.");
        }
    }
}