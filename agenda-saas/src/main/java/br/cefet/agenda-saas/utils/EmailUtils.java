package br.cefet.agendaSaas.utils;

import java.time.format.DateTimeFormatter;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import br.cefet.agendaSaas.model.entidades.Agendamento;
import br.cefet.agendaSaas.model.entidades.Cliente;
import br.cefet.agendaSaas.model.entidades.Prestador;
import br.cefet.agendaSaas.model.entidades.Servico;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;


public class EmailUtils {
	private static final Session session;
	private static final String FROM;
	private static final ExecutorService executor = Executors.newFixedThreadPool(2);

	static {
		String host = System.getenv().getOrDefault("MAIL_SMTP_HOST", "localhost");
		String port = System.getenv().getOrDefault("MAIL_SMTP_PORT", "25");
		String user = System.getenv().getOrDefault("MAIL_SMTP_USER", "");
		String pass = System.getenv().getOrDefault("MAIL_SMTP_PASSWORD", "");
		FROM = System.getenv().getOrDefault("MAIL_FROM", "no-reply@agendasaas.local");

		Properties props = new Properties();
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.port", port);
		props.put("mail.smtp.auth", !user.isEmpty() ? "true" : "false");
		props.put("mail.smtp.starttls.enable", System.getenv().getOrDefault("MAIL_SMTP_STARTTLS", "true"));

		if (!user.isEmpty()) {
			session = Session.getInstance(props, new jakarta.mail.Authenticator() {
				@Override
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(user, pass);
				}
			});
		} else {
			session = Session.getInstance(props);
		}
	}

	public static void sendHtml(String to, String subject, String html) throws MessagingException {
		MessagingException lastEx = null;
		for (int attempt = 1; attempt <= 3; attempt++) {
			try {
				MimeMessage message = buildMessage(to, subject, html);
				Transport.send(message);
				return;
			} catch (MessagingException e) {
				lastEx = e;
				try {
					Thread.sleep(500L * attempt);
				} catch (InterruptedException ie) {
					Thread.currentThread().interrupt();
				}
			}
		}
		throw lastEx;
	}

	public static Future<?> sendHtmlAsync(String to, String subject, String html) {
		return executor.submit(() -> {
			try {
				sendHtml(to, subject, html);
			} catch (MessagingException e) {
				log error (System.err temporariamente)
				System.err.println("Erro ao enviar e-mail para " + to + ": " + e.getMessage());
			}
		});
	}

	private static MimeMessage buildMessage(String to, String subject, String htmlBody) throws MessagingException {
		MimeMessage message = new MimeMessage(session);
		message.setFrom(new InternetAddress(FROM));
		message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
		message.setSubject(subject, "UTF-8");

		MimeBodyPart body = new MimeBodyPart();
		body.setContent(htmlBody, "text/html; charset=UTF-8");

		MimeMultipart mp = new MimeMultipart();
		mp.addBodyPart(body);
		message.setContent(mp);

		return message;
	}

	public static void notifyAgendamentoAsync(Agendamento agendamento, Servico servico, Prestador prestador,
			Cliente cliente) {
		if (prestador != null && prestador.getEmail() != null && !prestador.getEmail().isEmpty()) {
			String assunto = "Novo agendamento - " + servico.getNome();
			String corpo = buildAgendamentoHtml(agendamento, servico, prestador, cliente);
			sendHtmlAsync(prestador.getEmail(), assunto, corpo);
		}

		if (cliente != null && cliente.getEmail() != null && !cliente.getEmail().isEmpty()) {
			String assunto = "Agendamento confirmado - " + servico.getNome();
			String corpo = buildAgendamentoHtml(agendamento, servico, prestador, cliente);
			sendHtmlAsync(cliente.getEmail(), assunto, corpo);
		}
	}

	private static String buildAgendamentoHtml(Agendamento ag, Servico servico, Prestador prestador, Cliente cliente) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		StringBuilder sb = new StringBuilder();
		sb.append("<html><body>");
		sb.append("<h2>Agendamento ").append(servico.getNome()).append("</h2>");
		if (cliente != null) {
			sb.append("<p><strong>Cliente:</strong> ").append(cliente.getNome()).append(" (")
					.append(cliente.getEmail()).append(")</p>");
		}
		if (prestador != null) {
			sb.append("<p><strong>Prestador:</strong> ").append(prestador.getNome()).append(" (")
					.append(prestador.getEmail()).append(")</p>");
		}
		sb.append("<p><strong>Data/Hora:</strong> ")
				.append(ag.getDataHora() != null ? ag.getDataHora().format(dtf) : "--")
				.append("</p>");
		sb.append("<p><strong>Status:</strong> ").append(ag.getStatus()).append("</p>");
		sb.append("<p>Por favor verifique na sua agenda.</p>");
		sb.append("</body></html>");
		return sb.toString();
	}

	public static void shutdown() {
		executor.shutdown();
	}

}
