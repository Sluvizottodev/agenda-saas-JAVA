package br.cefet.agendasaas.controller;

import br.cefet.agendasaas.model.entidades.Agendamento;
import br.cefet.agendasaas.model.entidades.Servico;
import br.cefet.agendasaas.service.AgendamentoService;
import br.cefet.agendasaas.utils.ValidationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/agendamentos")
public class AgendamentoController {

    private final AgendamentoService agendamentoService;

    public AgendamentoController(AgendamentoService agendamentoService) {
        this.agendamentoService = agendamentoService;
    }

    @GetMapping
    public ResponseEntity<List<Agendamento>> listarAgendamentos(@RequestParam Integer usuarioId, @RequestParam String tipoUsuario) {
        try {
            List<Agendamento> agendamentos = agendamentoService.listarAgendamentos(usuarioId, tipoUsuario);
            return ResponseEntity.ok(agendamentos);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @PostMapping
    public ResponseEntity<String> criarAgendamento(@RequestBody Agendamento agendamento) {
        try {
            agendamentoService.salvarAgendamento(agendamento);
            return ResponseEntity.ok("Agendamento criado com sucesso!");
        } catch (ValidationException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro interno ao criar agendamento.");
        }
    }

    @GetMapping("/servicos")
    public ResponseEntity<List<Servico>> listarServicosDisponiveis() {
        try {
            List<Servico> servicos = agendamentoService.listarServicosDisponiveis();
            return ResponseEntity.ok(servicos);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @PutMapping
    public ResponseEntity<String> atualizarAgendamento(@RequestBody Agendamento agendamento) {
        try {
            agendamentoService.salvarAgendamento(agendamento);
            return ResponseEntity.ok("Agendamento atualizado com sucesso!");
        } catch (ValidationException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro interno ao atualizar agendamento.");
        }
    }

    @GetMapping("/{id}")
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

    @DeleteMapping("/{id}")
    public ResponseEntity<String> removerAgendamento(@PathVariable int id) {
        try {
            agendamentoService.removerAgendamento(id);
            return ResponseEntity.ok("Agendamento removido com sucesso!");
        } catch (ValidationException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro interno ao remover agendamento.");
        }
    }
}