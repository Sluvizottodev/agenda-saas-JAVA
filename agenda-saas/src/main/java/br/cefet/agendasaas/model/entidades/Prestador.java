package br.cefet.agendasaas.model.entidades;
import br.cefet.agendasaas.model.enums.TipoUsuario;
import jakarta.persistence.*;

@Entity
@DiscriminatorValue("PRESTADOR")
public class Prestador extends Usuario {

    @Column(nullable = true)
    private String telefone;

    @Column(nullable = true)
    private String especializacao;

    @Column(nullable = false, unique = true)
    private String cnpj;

    public Prestador() {
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

