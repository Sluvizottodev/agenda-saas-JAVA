package br.cefet.agendasaas.dao;

import java.util.List;

import br.cefet.agendasaas.model.entidades.Agendamento;
import br.cefet.agendasaas.utils.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

public class AgendamentoDAO {

    private final GenericDAO<Agendamento, Integer> dao;

    public AgendamentoDAO() {
        this.dao = new GenericDAO<>(Agendamento.class);
    }

    public AgendamentoDAO(GenericDAO<Agendamento, Integer> dao) {
        this.dao = dao;
    }

    public Agendamento save(Agendamento a) {
        return dao.save(a);
    }

    public Agendamento findById(Integer id) {
        return dao.findById(id).orElse(null);
    }

    public boolean inserir(Agendamento agendamento) {
        Agendamento saved = save(agendamento);
        return saved != null && saved.getId() != null;
    }

    public Agendamento buscarPorId(int id) {
        return findById(id);
    }

    public boolean atualizar(Agendamento agendamento) {
        return dao.update(agendamento) != null;
    }

    public boolean remover(int id) {
        return deleteById(id);
    }

    public List<Agendamento> listarPorPrestador(int prestadorId) {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            TypedQuery<Agendamento> q = em.createQuery("SELECT a FROM Agendamento a WHERE a.prestadorId = :pid",
                    Agendamento.class);
            q.setParameter("pid", prestadorId);
            return q.getResultList();
        }
    }

    public List<Agendamento> listarPorCliente(int clienteId) {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            TypedQuery<Agendamento> q = em.createQuery("SELECT a FROM Agendamento a WHERE a.clienteId = :cid",
                    Agendamento.class);
            q.setParameter("cid", clienteId);
            return q.getResultList();
        }
    }

    public Agendamento update(Agendamento a) {
        return dao.update(a);
    }

    public boolean deleteById(int id) {
        return dao.deleteById(id) > 0;
    }
}
