package br.cefet.agendasaas.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.cefet.agendasaas.model.entidades.HorarioDisponivel;

@Repository
public interface HorarioDisponivelRepository extends JpaRepository<HorarioDisponivel, Integer> {

    List<HorarioDisponivel> findByPrestadorId(Integer prestadorId);

    List<HorarioDisponivel> findByData(LocalDate data);

    List<HorarioDisponivel> findByPrestadorIdAndData(Integer prestadorId, LocalDate data);

    List<HorarioDisponivel> findByDisponivel(boolean disponivel);

    List<HorarioDisponivel> findByPrestadorIdAndDisponivel(Integer prestadorId, boolean disponivel);

    List<HorarioDisponivel> findByDataAndDisponivel(LocalDate data, boolean disponivel);

    List<HorarioDisponivel> findByPrestadorIdAndDataAndDisponivel(Integer prestadorId, LocalDate data,
            boolean disponivel);
}