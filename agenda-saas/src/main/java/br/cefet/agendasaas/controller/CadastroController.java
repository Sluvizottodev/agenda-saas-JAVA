package br.cefet.agendasaas.controller;

import br.cefet.agendasaas.model.entidades.Cliente;
import br.cefet.agendasaas.model.entidades.Prestador;
import br.cefet.agendasaas.service.CadastroService;
import br.cefet.agendasaas.utils.ValidationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cadastro")
public class CadastroController {

    private final CadastroService cadastroService;

    public CadastroController(CadastroService cadastroService) {
        this.cadastroService = cadastroService;
    }

    @PostMapping("/cliente")
    public ResponseEntity<String> cadastrarCliente(@RequestBody Cliente cliente) {
        try {
            cadastroService.cadastrarCliente(cliente);
            return ResponseEntity.ok("Cliente cadastrado com sucesso!");
        } catch (ValidationException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro interno ao cadastrar cliente.");
        }
    }

    @PostMapping("/prestador")
    public ResponseEntity<String> cadastrarPrestador(@RequestBody Prestador prestador) {
        try {
            cadastroService.cadastrarPrestador(prestador);
            return ResponseEntity.ok("Prestador cadastrado com sucesso!");
        } catch (ValidationException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro interno ao cadastrar prestador.");
        }
    }
}