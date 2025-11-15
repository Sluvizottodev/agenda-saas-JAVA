package br.cefet.agendasaas.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.cefet.agendasaas.dao.GenericDAO;
import br.cefet.agendasaas.model.entidades.Notificacao;
import br.cefet.agendasaas.utils.ValidationException;

@Service
public class NotificacaoService {
    
    private static final Logger LOGGER = Logger.getLogger(NotificacaoService.class.getName());

    private final GenericDAO<Notificacao, Integer> dao;

    public NotificacaoService() {
        this.dao = new GenericDAO<>(Notificacao.class);
    }

    @Autowired
    public NotificacaoService(GenericDAO<Notificacao, Integer> dao) {
        this.dao = dao;
    }

    public void criarNotificacao(Notificacao notificacao) throws ValidationException {
        if (notificacao == null) {
            throw new ValidationException("Notificação não pode ser nula");
        }
        if (notificacao.getMensagem() == null || notificacao.getMensagem().trim().isEmpty()) {
            throw new ValidationException("Mensagem é obrigatória");
        }
        if (notificacao.getDestinatarioId() == null || notificacao.getDestinatarioId() <= 0) {
            throw new ValidationException("ID do destinatário é obrigatório");
        }
        
        notificacao.setDataEnvio(LocalDateTime.now());
        notificacao.setLida(false);
        
        Notificacao saved = dao.save(notificacao);
        if (saved == null) {
            throw new ValidationException("Erro ao salvar notificação");
        }
    }
    
    public void notificarAgendamentoCriado(int clienteId, int prestadorId, String nomeServico) {
        try {
            // Notificar cliente
            Notificacao notifCliente = new Notificacao();
            notifCliente.setDestinatarioId(clienteId);
            notifCliente.setTipo("AGENDAMENTO_CRIADO");
            notifCliente.setMensagem("Seu agendamento para o serviço " + nomeServico + " foi criado com sucesso!");
            criarNotificacao(notifCliente);
            
            // Notificar prestador
            Notificacao notifPrestador = new Notificacao();
            notifPrestador.setDestinatarioId(prestadorId);
            notifPrestador.setTipo("NOVO_AGENDAMENTO");
            notifPrestador.setMensagem("Você tem um novo agendamento para o serviço " + nomeServico + "!");
            criarNotificacao(notifPrestador);
        } catch (ValidationException e) {
            LOGGER.severe(() -> "Erro ao criar notificações de agendamento criado: " + e.getMessage());
        }
    }
    
    public void notificarAgendamentoCancelado(int clienteId, int prestadorId, String nomeServico) {
        try {
            // Notificar cliente
            Notificacao notifCliente = new Notificacao();
            notifCliente.setDestinatarioId(clienteId);
            notifCliente.setTipo("AGENDAMENTO_CANCELADO");
            notifCliente.setMensagem("Seu agendamento para o serviço " + nomeServico + " foi cancelado.");
            criarNotificacao(notifCliente);
            
            // Notificar prestador
            Notificacao notifPrestador = new Notificacao();
            notifPrestador.setDestinatarioId(prestadorId);
            notifPrestador.setTipo("AGENDAMENTO_CANCELADO");
            notifPrestador.setMensagem("O agendamento para o serviço " + nomeServico + " foi cancelado.");
            criarNotificacao(notifPrestador);
        } catch (ValidationException e) {
            LOGGER.severe(() -> "Erro ao criar notificações de cancelamento: " + e.getMessage());
        }
    }

    public List<Notificacao> listarPorUsuario(int usuarioId) {
        return dao.findWithQuery("FROM Notificacao n WHERE n.destinatarioId = ? ORDER BY n.dataEnvio DESC", usuarioId);
    }
    
    public List<Notificacao> listarNaoLidasPorUsuario(int usuarioId) {
        return dao.findWithQuery("FROM Notificacao n WHERE n.destinatarioId = ? AND n.lida = false ORDER BY n.dataEnvio DESC", usuarioId);
    }
    
    public void marcarComoLida(int notificacaoId) throws ValidationException {
        Notificacao notificacao = dao.findById(notificacaoId).orElse(null);
        if (notificacao == null) {
            throw new ValidationException("Notificação não encontrada");
        }
        notificacao.setLida(true);
        dao.update(notificacao);
    }
    
    public void marcarTodasComoLidas(int usuarioId) {
        List<Notificacao> notificacoes = listarNaoLidasPorUsuario(usuarioId);
        for (Notificacao notif : notificacoes) {
            notif.setLida(true);
            dao.update(notif);
        }
    }

    public Notificacao buscarPorId(int id) {
        return dao.findById(id).orElse(null);
    }

    public List<Notificacao> listarTodos() {
        return dao.findAll();
    }

    public boolean remover(int id) {
        return dao.deleteById(id) > 0;
    }
}
