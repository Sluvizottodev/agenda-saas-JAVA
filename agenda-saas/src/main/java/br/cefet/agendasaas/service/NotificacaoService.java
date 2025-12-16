package br.cefet.agendasaas.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.cefet.agendasaas.model.entidades.Notificacao;
import br.cefet.agendasaas.repository.NotificacaoRepository;
import br.cefet.agendasaas.utils.ValidationException;

@Service
public class NotificacaoService {

    private static final Logger LOGGER = Logger.getLogger(NotificacaoService.class.getName());

    @Autowired
    private NotificacaoRepository notificacaoRepository;

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

        Notificacao saved = notificacaoRepository.save(notificacao);
        if (saved == null) {
            throw new ValidationException("Erro ao salvar notificação");
        }
    }

    public void notificarAgendamentoCriado(int clienteId, int prestadorId, String nomeServico) {
        try {
            Notificacao notifCliente = new Notificacao();
            notifCliente.setDestinatarioId(clienteId);
            notifCliente.setTipo("AGENDAMENTO_CRIADO");
            notifCliente.setMensagem("Seu agendamento para o serviço " + nomeServico + " foi criado com sucesso!");
            criarNotificacao(notifCliente);

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
            Notificacao notifCliente = new Notificacao();
            notifCliente.setDestinatarioId(clienteId);
            notifCliente.setTipo("AGENDAMENTO_CANCELADO");
            notifCliente.setMensagem("Seu agendamento para o serviço " + nomeServico + " foi cancelado.");
            criarNotificacao(notifCliente);

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
        return notificacaoRepository.findByDestinatarioIdOrderByDataEnvioDesc(usuarioId);
    }

    public List<Notificacao> listarNaoLidasPorUsuario(int usuarioId) {
        return notificacaoRepository.findByDestinatarioIdAndLidaOrderByDataEnvioDesc(usuarioId, false);
    }

    public void marcarComoLida(int notificacaoId) throws ValidationException {
        Optional<Notificacao> notificacaoOpt = notificacaoRepository.findById(notificacaoId);
        if (notificacaoOpt.isEmpty()) {
            throw new ValidationException("Notificação não encontrada");
        }
        Notificacao notificacao = notificacaoOpt.get();
        notificacao.setLida(true);
        notificacaoRepository.save(notificacao);
    }

    public void marcarTodasComoLidas(int usuarioId) {
        List<Notificacao> notificacoes = listarNaoLidasPorUsuario(usuarioId);
        for (Notificacao notif : notificacoes) {
            notif.setLida(true);
            notificacaoRepository.save(notif);
        }
    }

    public Optional<Notificacao> buscarPorId(int id) {
        return notificacaoRepository.findById(id);
    }

    public List<Notificacao> listarTodos() {
        return notificacaoRepository.findAll();
    }

    public void remover(int id) throws ValidationException {
        if (!notificacaoRepository.existsById(id)) {
            throw new ValidationException("Notificação não encontrada");
        }
        notificacaoRepository.deleteById(id);
    }
}
