package br.cefet.agendasaas.model.entidades;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "pagamento")
public class Pagamento extends Entidade {

	@Column(name = "agendamento_id")
	private Integer agendamentoId;

	private BigDecimal valor;

	private String status;

	@Column(name = "data_pagamento")
	private LocalDateTime dataPagamento;

	public Integer getAgendamentoId() {
		return agendamentoId;
	}

	public void setAgendamentoId(Integer agendamentoId) {
		this.agendamentoId = agendamentoId;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public LocalDateTime getDataPagamento() {
		return dataPagamento;
	}

	public void setDataPagamento(LocalDateTime dataPagamento) {
		this.dataPagamento = dataPagamento;
	}

}

