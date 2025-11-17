package br.cefet.agendasaas.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.cefet.agendasaas.model.entidades.Prestador;

@Repository
public interface PrestadorRepository extends JpaRepository<Prestador, Integer> {

    Optional<Prestador> findByEmail(String email);

    @Query("SELECT p FROM Prestador p WHERE p.especializacao LIKE %:especializacao%")
    List<Prestador> findByEspecializacaoContaining(@Param("especializacao") String especializacao);

    boolean existsByEmail(String email);

    boolean existsByCnpj(String cnpj);
}