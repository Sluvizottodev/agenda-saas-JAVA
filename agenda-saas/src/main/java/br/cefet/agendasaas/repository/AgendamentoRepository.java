package br.cefet.agendasaas.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.cefet.agendasaas.model.entidades.Agendamento;

@Repository
public interface AgendamentoRepository extends JpaRepository<Agendamento, Integer> {
    
    @Query("SELECT a FROM Agendamento a WHERE a.clienteId = :clienteId ORDER BY a.dataHora DESC")
    List<Agendamento> findByClienteId(@Param("clienteId") Integer clienteId);
    
    @Query("SELECT a FROM Agendamento a WHERE a.prestadorId = :prestadorId ORDER BY a.dataHora DESC")
    List<Agendamento> findByPrestadorId(@Param("prestadorId") Integer prestadorId);
    
    @Query("SELECT a FROM Agendamento a WHERE a.status = :status ORDER BY a.dataHora")
    List<Agendamento> findByStatus(@Param("status") String status);
    
    @Query("SELECT a FROM Agendamento a WHERE a.dataHora BETWEEN :inicio AND :fim ORDER BY a.dataHora")
    List<Agendamento> findByDataHoraBetween(@Param("inicio") LocalDateTime inicio, @Param("fim") LocalDateTime fim);
}