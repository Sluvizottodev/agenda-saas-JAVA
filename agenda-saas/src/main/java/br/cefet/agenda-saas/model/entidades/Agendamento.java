package br.cefet.agendaSaas.model.entidades;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "agendamento")
public class Agendamento extends Entidade {
    @Column(name = "cliente_id")
    private Integer clienteId;

    @Column(name = "prestador_id")
    private Integer prestadorId;

    @Column(name = "servico_id")
    private Integer servicoId;

    @Column(name = "data_hora")
    private LocalDateTime dataHora;

    private String status;

    public Agendamento() {
    }

    public Agendamento(int clienteId, int prestadorId, int servicoId, LocalDateTime dataHora, String status) {
        this.clienteId = clienteId;
        this.prestadorId = prestadorId;
        this.servicoId = servicoId;
        this.dataHora = dataHora;
        this.status = status;
    }

    // Getters e Setters
    // id herdado de Entidade

    public Integer getClienteId() {
        return clienteId;
    }

    public void setClienteId(Integer clienteId) {
        this.clienteId = clienteId;
    }

    public Integer getPrestadorId() {
        return prestadorId;
    }

    public void setPrestadorId(Integer prestadorId) {
        this.prestadorId = prestadorId;
    }

    public Integer getServicoId() {
        return servicoId;
    }

    public void setServicoId(Integer servicoId) {
        this.servicoId = servicoId;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Agendamento{" +
                "id=" + id +
                ", clienteId=" + clienteId +
                ", prestadorId=" + prestadorId +
                ", servicoId=" + servicoId +
                ", dataHora=" + dataHora +
                ", status='" + status + '\'' +
                '}';
    }
}
