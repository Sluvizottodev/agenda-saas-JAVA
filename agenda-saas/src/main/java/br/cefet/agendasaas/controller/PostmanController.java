package br.cefet.agendasaas.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/postman")
public class PostmanController {

    private static final Logger logger = LoggerFactory.getLogger(PostmanController.class);

    @GetMapping
    public ResponseEntity<String> handleGet() {
        logger.info("Entrou no GET");
        return ResponseEntity.ok("{\"mensagem\": \"Requisição GET recebida com sucesso!\"}");
    }

    @PostMapping
    public ResponseEntity<String> handlePost() {
        logger.info("Entrou no POST");
        return ResponseEntity.ok("{\"mensagem\": \"Requisição POST recebida com sucesso!\"}");
    }

    @PutMapping
    public ResponseEntity<String> handlePut() {
        logger.info("Entrou no PUT");
        return ResponseEntity.ok("{\"mensagem\": \"Requisição PUT recebida com sucesso!\"}");
    }

    @DeleteMapping
    public ResponseEntity<String> handleDelete() {
        logger.info("Entrou no DELETE");
        return ResponseEntity.ok("{\"mensagem\": \"Requisição DELETE recebida com sucesso!\"}");
    }
}