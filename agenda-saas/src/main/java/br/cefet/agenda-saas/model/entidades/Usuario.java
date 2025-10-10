package br.cefet.agendaSaas.model.entidades;

import br.cefet.agendaSaas.model.enums.TipoUsuario;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Transient;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_usuario", discriminatorType = DiscriminatorType.STRING)
public abstract class Usuario extends Entidade {

    protected String nome;
    protected String email;
    protected String senha;

    @Transient
    protected TipoUsuario tipo;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome){
        this.nome = nome;
    }

    public String getEmail(){
        return email;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getSenha(){
        return senha;
    }

    public void setSenha(String senha){
        this.senha = senha;
    }

    public TipoUsuario getTipo(){
        return tipo;
    }

    public void setTipo(TipoUsuario tipo){
        this.tipo = tipo;
    }
}
