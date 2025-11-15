package br.cefet.agendasaas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.cefet.agendasaas.model.entidades.HorarioDisponivel;

@Repository
public interface HorarioDisponivelRepository extends JpaRepository<HorarioDisponivel, Integer> {
}