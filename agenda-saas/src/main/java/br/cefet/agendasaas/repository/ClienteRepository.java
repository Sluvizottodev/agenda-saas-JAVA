package br.cefet.agendasaas.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.cefet.agendasaas.model.entidades.Cliente;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
    
    Optional<Cliente> findByEmail(String email);
    
    Optional<Cliente> findByTelefone(String telefone);
    
    boolean existsByEmail(String email);
    
    boolean existsByTelefone(String telefone);
}