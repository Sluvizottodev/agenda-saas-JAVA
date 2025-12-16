package br.cefet.agendasaas.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import br.cefet.agendasaas.utils.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

public class GenericDAO<T, ID extends Serializable> {

    private final Class<T> entityClass;

    public GenericDAO(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    public Optional<T> findById(ID id) {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            T entity = em.find(entityClass, id);
            return Optional.ofNullable(entity);
        }
    }

    public List<T> findAll() {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            String ql = "from " + entityClass.getSimpleName();
            TypedQuery<T> q = em.createQuery(ql, entityClass);
            return q.getResultList();
        }
    }

    public T save(T entity) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(entity);
            tx.commit();
            return entity;
        } catch (RuntimeException e) {
            if (tx.isActive())
                tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public T update(T entity) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            T merged = em.merge(entity);
            tx.commit();
            return merged;
        } catch (RuntimeException e) {
            if (tx.isActive())
                tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public void delete(T entity) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            T merged = em.contains(entity) ? entity : em.merge(entity);
            em.remove(merged);
            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive())
                tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public int deleteById(ID id) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            T entity = em.find(entityClass, id);
            if (entity == null) {
                tx.commit();
                return 0;
            }
            em.remove(entity);
            tx.commit();
            return 1;
        } catch (RuntimeException e) {
            if (tx.isActive())
                tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public List<T> findWithQuery(String ql, Object... params) {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            TypedQuery<T> q = em.createQuery(ql, entityClass);
            for (int i = 0; i < params.length; i++) {
                q.setParameter(i + 1, params[i]);
            }
            return q.getResultList();
        }
    }

    public List<T> findAll(int page, int size) {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            String ql = "from " + entityClass.getSimpleName();
            TypedQuery<T> q = em.createQuery(ql, entityClass);
            q.setFirstResult(page * size);
            q.setMaxResults(size);
            return q.getResultList();
        }
    }

    public long count() {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            String ql = "select count(e) from " + entityClass.getSimpleName() + " e";
            TypedQuery<Long> q = em.createQuery(ql, Long.class);
            return q.getSingleResult();
        }
    }

    public List<T> findWithQuery(String ql, int page, int size, Object... params) {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            TypedQuery<T> q = em.createQuery(ql, entityClass);
            for (int i = 0; i < params.length; i++) {
                q.setParameter(i + 1, params[i]);
            }
            q.setFirstResult(page * size);
            q.setMaxResults(size);
            return q.getResultList();
        }
    }

}
