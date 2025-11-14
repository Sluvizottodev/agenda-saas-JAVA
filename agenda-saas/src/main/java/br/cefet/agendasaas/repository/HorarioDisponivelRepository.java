package br.cefet.agendasaas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import br.cefet.agendasaas.model.entidades.HorarioDisponivel;

public interface HorarioDisponivelRepository extends JpaRepository<HorarioDisponivel, Integer> {
}