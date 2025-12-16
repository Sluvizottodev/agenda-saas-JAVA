package br.cefet.agendasaas.dto;

import java.io.Serializable;

/**
 * DTO para resposta de usuário autenticado
 * Contém informações públicas do usuário e seu role
 */
public class UsuarioAuthResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private String nome;
    private String email;
    private String role; // CLIENTE ou PRESTADOR
    private String mensagem;

    public UsuarioAuthResponse() {
    }

    public UsuarioAuthResponse(Integer id, String nome, String email, String role) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.role = role;
    }

    public UsuarioAuthResponse(Integer id, String nome, String email, String role, String mensagem) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.role = role;
        this.mensagem = mensagem;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    @Override
    public String toString() {
        return "UsuarioAuthResponse [id=" + id + ", nome=" + nome + ", email=" + email + ", role=" + role
                + "]";
    }
}
