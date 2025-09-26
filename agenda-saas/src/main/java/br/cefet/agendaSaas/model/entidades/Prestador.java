package br.cefet.agendaSaas.model.entidades;

import br.cefet.agendaSaas.model.enums.TipoUsuario;

public class Prestador extends Usuario {
    private String telefone;
    private String especializacao;
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
