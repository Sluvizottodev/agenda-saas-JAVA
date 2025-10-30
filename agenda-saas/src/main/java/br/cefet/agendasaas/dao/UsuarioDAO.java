package br.cefet.agendasaas.dao;

import java.util.List;
import java.util.Optional;

import br.cefet.agendasaas.model.entidades.Usuario;

public class UsuarioDAO {

    private final GenericDAO<Usuario, Integer> dao;

    public UsuarioDAO() {
        this.dao = new GenericDAO<>(Usuario.class);
    }

    public UsuarioDAO(GenericDAO<Usuario, Integer> dao) {
        this.dao = dao;
    }

    public Usuario save(Usuario usuario) {
        return dao.save(usuario);
    }

    public Optional<Usuario> findById(Integer id) {
        return dao.findById(id);
    }

    public boolean inserir(Usuario usuario) {
        Usuario saved = save(usuario);
        return saved != null && saved.getId() != null;
    }

    public Usuario buscarPorId(int id) {
        return findById(id).orElse(null);
    }

    public List<Usuario> listarTodos() {
        return findAll();
    }

    public boolean atualizar(Usuario usuario) {
        Usuario updated = dao.update(usuario);
        return updated != null;
    }

    public boolean remover(int id) {
        return deleteById(id);
    }

    public List<Usuario> findAll() {
        return dao.findAll();
    }

    public boolean deleteById(Integer id) {
        return dao.deleteById(id) > 0;
    }

    public Usuario buscarPorEmailSenha(String email, String senha) {
        try (jakarta.persistence.EntityManager em = br.cefet.agendasaas.utils.JPAUtil.getEntityManager()) {
            jakarta.persistence.TypedQuery<Usuario> q = em.createQuery(
                    "SELECT u FROM Usuario u WHERE u.email = :email AND u.senha = :senha", Usuario.class);
            q.setParameter("email", email);
            q.setParameter("senha", senha);
            java.util.List<Usuario> lista = q.getResultList();
            return lista.isEmpty() ? null : lista.get(0);
        }
    }

}
