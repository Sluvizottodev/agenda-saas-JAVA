package br.cefet.agendasaas.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.cefet.agendasaas.model.entidades.Servico;

@Repository
public interface ServicoRepository extends JpaRepository<Servico, Integer> {
    
    @Query("SELECT s FROM Servico s WHERE s.prestadorId = :prestadorId")
    List<Servico> findByPrestadorId(@Param("prestadorId") Integer prestadorId);
    
    @Query("SELECT s FROM Servico s WHERE s.nome LIKE %:nome%")
    List<Servico> findByNomeContaining(@Param("nome") String nome);
    
    @Query("SELECT s FROM Servico s WHERE s.preco BETWEEN :precoMin AND :precoMax")
    List<Servico> findByPrecoBetween(@Param("precoMin") Double precoMin, @Param("precoMax") Double precoMax);
}