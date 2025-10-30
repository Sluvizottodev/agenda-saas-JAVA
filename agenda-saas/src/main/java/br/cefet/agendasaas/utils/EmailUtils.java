package br.cefet.agendasaas.utils;

public final class EmailUtils {

	private EmailUtils() {

	}

	public static void sendHtml(String to, String subject, String html) {

		System.out.println("[EmailUtils] sendHtml -> to=" + to + " subject=" + subject);
	}

	public static void sendHtmlAsync(String to, String subject, String html) {
		sendHtml(to, subject, html);
	}

	public static void notifyAgendamentoAsync(Object agendamento, Object servico, Object prestador, Object cliente) {

	}

}
