package br.cefet.agendaSaas.model.entidades;

import br.cefet.agendaSaas.model.enums.TipoUsuario;
import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) 
@DiscriminatorColumn(name = "tipo_usuario", discriminatorType = DiscriminatorType.STRING)
public abstract class Usuario extends Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected int id;

    protected String nome;
    protected String email;
    protected String senha;


    @Transient
    protected TipoUsuario tipo;

    public int getId() {
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

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
