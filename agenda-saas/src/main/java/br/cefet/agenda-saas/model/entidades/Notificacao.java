package br.cefet.agendaSaas.model.entidades;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "notificacao")
public class Notificacao extends Entidade {

	@Column(name = "destinatario_id")
	private Integer destinatarioId;

	private String tipo;

	private String mensagem;

	private boolean lida;

	@Column(name = "data_envio")
	private LocalDateTime dataEnvio;

	public Integer getDestinatarioId() {
		return destinatarioId;
	}

	public void setDestinatarioId(Integer destinatarioId) {
		this.destinatarioId = destinatarioId;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	public boolean isLida() {
		return lida;
	}

	public void setLida(boolean lida) {
		this.lida = lida;
	}

	public LocalDateTime getDataEnvio() {
		return dataEnvio;
	}

	public void setDataEnvio(LocalDateTime dataEnvio) {
		this.dataEnvio = dataEnvio;
	}

}
