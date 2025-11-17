package br.cefet.agendasaas.dto;

public class JwtResponse {

    private String token;
    private String tipo = "Bearer";
    private String email;
    private String nome;
    private String tipoUsuario;
    private Integer id;

    public JwtResponse(String accessToken, String email, String nome, String tipoUsuario, Integer id) {
        this.token = accessToken;
        this.email = email;
        this.nome = nome;
        this.tipoUsuario = tipoUsuario;
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}