package br.cefet.agendaSaas.model.entidades;

import br.cefet.agendaSaas.model.enums.TipoUsuario;
import jakarta.persistence.*;

@Entity
@DiscriminatorValue("CLIENTE")
public class Cliente extends Usuario {

    @Column(nullable = false, unique = true)
    private String cpf;

    public Cliente() {
        this.tipo = TipoUsuario.CLIENTE;
    }
    
    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }
}
