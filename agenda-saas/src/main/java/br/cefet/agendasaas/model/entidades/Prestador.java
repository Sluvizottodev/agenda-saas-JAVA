package br.cefet.agendasaas.model.entidades;

import br.cefet.agendasaas.model.enums.TipoUsuario;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("PRESTADOR")
public class Prestador extends Usuario {

    @Column(nullable = true)
    private String telefone;

    @Column(nullable = true)
    private String especializacao;

    @Column(nullable = true, unique = true)
    private String cnpj;

    public Prestador() {
        this.tipo = TipoUsuario.PRESTADOR;
    }

    public Prestador(String nome, String email, String senha, String telefone, String especializacao, String cnpj) {
        super(nome, email, senha);
        this.telefone = telefone;
        this.especializacao = especializacao;
        this.cnpj = cnpj;
        this.tipo = TipoUsuario.PRESTADOR;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEspecializacao() {
        return especializacao;
    }

    public void setEspecializacao(String especializacao) {
        this.especializacao = especializacao;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }
}
