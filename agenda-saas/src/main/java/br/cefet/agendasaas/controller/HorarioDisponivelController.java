package br.cefet.agendasaas.controller;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.cefet.agendasaas.model.entidades.HorarioDisponivel;

@RestController
@RequestMapping("/api/horarios")
public class HorarioDisponivelController {

    private static final Logger logger = LoggerFactory.getLogger(HorarioDisponivelController.class);
    private static final String ERRO_HORA_INICIO = "Hora de início não pode ser após a hora de fim";
    private static final String ERRO_HORARIO_PASSADO = "Não é possível criar ou atualizar horários no passado";

    private final JpaRepository<HorarioDisponivel, Integer> repository;

    public HorarioDisponivelController(JpaRepository<HorarioDisponivel, Integer> repository) {
        this.repository = repository;
    }

    @GetMapping
    public ResponseEntity<List<HorarioDisponivel>> listarTodos() {
        logger.info("Listando todos os horários disponíveis");
        return ResponseEntity.ok(repository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<HorarioDisponivel> buscarPorId(@PathVariable Integer id) {
        logger.info("Buscando horário com ID: {}", id);
        if (id == null) {
            return ResponseEntity.badRequest().build();
        }
        Optional<HorarioDisponivel> horario = repository.findById(id);
        return horario.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<String> criar(@RequestBody HorarioDisponivel horario) {
        logger.info("Criando novo horário: {}", horario);

        if (horario.getHoraInicio().isAfter(horario.getHoraFim())) {
            logger.error(ERRO_HORA_INICIO);
            return ResponseEntity.badRequest().body(ERRO_HORA_INICIO);
        }

        if (horario.isHorarioPassado()) {
            logger.error(ERRO_HORARIO_PASSADO);
            return ResponseEntity.badRequest().body(ERRO_HORARIO_PASSADO);
        }

        HorarioDisponivel novoHorario = repository.save(horario);
        logger.info("Horário criado com sucesso: {}", novoHorario);
        return ResponseEntity.ok("Horário criado com sucesso");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> atualizar(@PathVariable Integer id, @RequestBody HorarioDisponivel horario) {
        logger.info("Atualizando horário com ID: {}", id);

        if (id == null || !repository.existsById(id)) {
            logger.error("Horário com ID: {} não encontrado", id);
            return ResponseEntity.notFound().build();
        }

        if (horario.getHoraInicio().isAfter(horario.getHoraFim())) {
            logger.error(ERRO_HORA_INICIO);
            return ResponseEntity.badRequest().body(ERRO_HORA_INICIO);
        }

        if (horario.isHorarioPassado()) {
            logger.error(ERRO_HORARIO_PASSADO);
            return ResponseEntity.badRequest().body(ERRO_HORARIO_PASSADO);
        }

        horario.setId(id);
        HorarioDisponivel horarioAtualizado = repository.save(horario);
        logger.info("Horário atualizado com sucesso: {}", horarioAtualizado);
        return ResponseEntity.ok("Horário atualizado com sucesso");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> remover(@PathVariable Integer id) {
        logger.info("Removendo horário com ID: {}", id);

        if (id == null || !repository.existsById(id)) {
            logger.error("Horário com ID: {} não encontrado", id);
            return ResponseEntity.notFound().build();
        }

        repository.deleteById(id);
        logger.info("Horário removido com sucesso");
        return ResponseEntity.ok("Horário removido com sucesso");
    }
}