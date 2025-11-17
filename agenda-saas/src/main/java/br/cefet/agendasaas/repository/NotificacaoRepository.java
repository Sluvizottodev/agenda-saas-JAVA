package br.cefet.agendasaas.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.cefet.agendasaas.model.entidades.Notificacao;

@Repository
public interface NotificacaoRepository extends JpaRepository<Notificacao, Integer> {

    List<Notificacao> findByDestinatarioIdOrderByDataEnvioDesc(Integer destinatarioId);

    List<Notificacao> findByDestinatarioIdAndLidaOrderByDataEnvioDesc(Integer destinatarioId, boolean lida);

    List<Notificacao> findByTipo(String tipo);
}