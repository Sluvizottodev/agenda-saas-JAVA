package br.cefet.agendasaas.dao;

import java.util.List;
import java.util.Optional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import br.cefet.agendasaas.model.entidades.Usuario;
import br.cefet.agendasaas.utils.JPAUtil;

public class UsuarioDAO {

    private final GenericDAO<Usuario, Integer> dao = new GenericDAO<>(Usuario.class);

    // default constructor

    public Usuario save(Usuario usuario) {
        return dao.save(usuario);
    }

    public Optional<Usuario> findById(Integer id) {
        return dao.findById(id);
    }

    // compatibility methods (legacy API)
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
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Usuario> q = em.createQuery("SELECT u FROM Usuario u WHERE u.email = :email AND u.senha = :senha", Usuario.class);
            q.setParameter("email", email);
            q.setParameter("senha", senha);
            List<Usuario> list = q.getResultList();
            return list.isEmpty() ? null : list.get(0);
        } finally {
            em.close();
        }
    }

}

