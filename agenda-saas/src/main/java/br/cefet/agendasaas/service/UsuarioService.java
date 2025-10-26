package br.cefet.agendasaas.service;

import java.util.List;

import br.cefet.agendasaas.dao.UsuarioDAO;
import br.cefet.agendasaas.model.entidades.Usuario;

public class UsuarioService {

    private final UsuarioDAO usuarioDAO;

    public UsuarioService() {
        this.usuarioDAO = new UsuarioDAO();
    }

    public Usuario autenticar(String email, String senha) {
        return usuarioDAO.buscarPorEmailSenha(email, senha);
    }

    public boolean cadastrar(Usuario usuario) {
        return usuarioDAO.inserir(usuario);
    }

    public Usuario buscarPorId(int id) {
        return usuarioDAO.buscarPorId(id);
    }

    public List<Usuario> listarTodos() {
        return usuarioDAO.listarTodos();
    }

    public boolean atualizar(Usuario usuario) {
        return usuarioDAO.atualizar(usuario);
    }

    public boolean remover(int id) {
        return usuarioDAO.remover(id);
    }

}
