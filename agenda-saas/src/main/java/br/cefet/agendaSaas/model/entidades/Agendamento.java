package br.cefet.agendaSaas.model.entidades;

import java.time.LocalDateTime;

public class Agendamento {
    private int id;
    private int clienteId;
    private int prestadorId;
    private int servicoId;
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
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClienteId() {
        return clienteId;
    }

    public void setClienteId(int clienteId) {
        this.clienteId = clienteId;
    }

    public int getPrestadorId() {
        return prestadorId;
    }

    public void setPrestadorId(int prestadorId) {
        this.prestadorId = prestadorId;
    }

    public int getServicoId() {
        return servicoId;
    }

    public void setServicoId(int servicoId) {
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
