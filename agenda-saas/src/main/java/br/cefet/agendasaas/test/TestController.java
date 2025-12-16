package br.cefet.agendasaas.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class TestController {
    
    @GetMapping("/test")
    public String test() {
        return "Spring Boot funcionando!";
    }
    
    public static void main(String[] args) {
        SpringApplication.run(TestController.class, args);
    }
}