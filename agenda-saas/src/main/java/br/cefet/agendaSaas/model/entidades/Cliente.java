package br.cefet.agendaSaas.model.entidades;

import br.cefet.agendaSaas.model.enums.TipoUsuario;

public class Cliente extends Usuario {
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
