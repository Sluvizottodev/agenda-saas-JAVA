package br.cefet.agendaSaas.model.entidades;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "horario_disponivel")
public class HorarioDisponivel extends Entidade {
    @Column(name = "prestador_id")
    private Integer prestadorId;

    private LocalDate data;

    @Column(name = "hora_inicio")
    private LocalTime horaInicio;

    @Column(name = "hora_fim")
    private LocalTime horaFim;

    private boolean disponivel;

    public HorarioDisponivel() {
    }

    public HorarioDisponivel(int prestadorId, LocalDate data, LocalTime horaInicio, LocalTime horaFim) {
        this.prestadorId = prestadorId;
        this.data = data;
        this.horaInicio = horaInicio;
        this.horaFim = horaFim;
        this.disponivel = true;
    }

    public HorarioDisponivel(int prestadorId, LocalDate data, LocalTime horaInicio, LocalTime horaFim,
            boolean disponivel) {
        this.prestadorId = prestadorId;
        this.data = data;
        this.horaInicio = horaInicio;
        this.horaFim = horaFim;
        this.disponivel = disponivel;
    }

    // Getters e Setters
    // id herdado de Entidade

    public Integer getPrestadorId() {
        return prestadorId;
    }

    public void setPrestadorId(Integer prestadorId) {
        this.prestadorId = prestadorId;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        if (data == null) {
            throw new IllegalArgumentException("Data não pode ser nula");
        }
        this.data = data;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        if (horaInicio == null) {
            throw new IllegalArgumentException("Hora de início não pode ser nula");
        }
        this.horaInicio = horaInicio;
    }

    public LocalTime getHoraFim() {
        return horaFim;
    }

    public void setHoraFim(LocalTime horaFim) {
        if (horaFim == null) {
            throw new IllegalArgumentException("Hora de fim não pode ser nula");
        }
        if (this.horaInicio != null && horaFim.isBefore(this.horaInicio)) {
            throw new IllegalArgumentException("Hora de fim deve ser posterior à hora de início");
        }
        this.horaFim = horaFim;
    }

    public boolean isDisponivel() {
        return disponivel;
    }

    public void setDisponivel(boolean disponivel) {
        this.disponivel = disponivel;
    }

    public boolean isHorarioConflitante(LocalTime horaInicio, LocalTime horaFim) {
        return !(horaFim.isBefore(this.horaInicio) || horaInicio.isAfter(this.horaFim));
    }

    public boolean isHorarioPassado() {
        return data.isBefore(LocalDate.now()) ||
                (data.equals(LocalDate.now()) && horaFim.isBefore(LocalTime.now()));
    }

    @Override
    public String toString() {
        return "HorarioDisponivel{" +
                "id=" + id +
                ", prestadorId=" + prestadorId +
                ", data=" + data +
                ", horaInicio=" + horaInicio +
                ", horaFim=" + horaFim +
                ", disponivel=" + disponivel +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        HorarioDisponivel that = (HorarioDisponivel) obj;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}
