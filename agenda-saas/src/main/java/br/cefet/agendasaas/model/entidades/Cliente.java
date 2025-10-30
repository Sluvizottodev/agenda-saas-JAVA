package br.cefet.agendasaas.model.entidades;

import br.cefet.agendasaas.model.enums.TipoUsuario;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("CLIENTE")
public class Cliente extends Usuario {

    @Column(nullable = false, unique = true)
    private String cpf;

    public Cliente() {
        this.tipo = TipoUsuario.CLIENTE;
    }

    public Cliente(String nome, String email, String senha, String cpf) {
        super(nome, email, senha);
        this.cpf = cpf;
        this.tipo = TipoUsuario.CLIENTE;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }
}
