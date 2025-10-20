package br.cefet.agendasaas.utils;

/**
 * EmailUtils simplificado: implementa no-op para envio de e-mail durante o
 * desenvolvimento
 * (evita adicionar dependÃªncia de Jakarta Mail ao build). Quando necessÃ¡rio,
 * reimplemente com
 * uma biblioteca de envio de e-mail e atualize o pom.xml.
 */
public final class EmailUtils {

	private EmailUtils() {
		// util class
	}

	public static void sendHtml(String to, String subject, String html) {
		// no-op: apenas loga para stdout durante desenvolvimento
		System.out.println("[EmailUtils] sendHtml -> to=" + to + " subject=" + subject);
	}

	public static void sendHtmlAsync(String to, String subject, String html) {
		sendHtml(to, subject, html);
	}

	public static void notifyAgendamentoAsync(Object agendamento, Object servico, Object prestador, Object cliente) {
		// no-op
	}

}

