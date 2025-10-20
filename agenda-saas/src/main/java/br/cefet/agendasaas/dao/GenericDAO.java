package br.cefet.agendasaas.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import br.cefet.agendasaas.utils.HibernateUtil;

public class GenericDAO<T, ID extends Serializable> {

    private final Class<T> entityClass;

    public GenericDAO(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    public Optional<T> findById(ID id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            T entity = session.get(entityClass, id);
            return Optional.ofNullable(entity);
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public List<T> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "from " + entityClass.getSimpleName();
            Query<T> q = session.createQuery(hql, entityClass);
            return q.list();
        }
    }

    public T save(T entity) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(entity);
            tx.commit();
            return entity;
        } catch (RuntimeException e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    public T update(T entity) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            T merged = (T) session.merge(entity);
            tx.commit();
            return merged;
        } catch (RuntimeException e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    public void delete(T entity) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.remove(entity);
            tx.commit();
        } catch (RuntimeException e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    public int deleteById(ID id) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            T entity = session.get(entityClass, id);
            if (entity == null) {
                tx.commit();
                return 0;
            }
            session.remove(entity);
            tx.commit();
            return 1;
        } catch (RuntimeException e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    public List<T> findWithQuery(String hql, Object... params) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<T> q = session.createQuery(hql, entityClass);
            for (int i = 0; i < params.length; i++) {
                q.setParameter(i + 1, params[i]);
            }
            return q.list();
        }
    }

// return paginado
    public List<T> findAll(int page, int size) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "from " + entityClass.getSimpleName();
            Query<T> q = session.createQuery(hql, entityClass);
            q.setFirstResult(page * size);
            q.setMaxResults(size);
            return q.list();
        }
    }

// count registros de entyty
    public long count() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "select count(e) from " + entityClass.getSimpleName() + " e";
            Query<Long> q = session.createQuery(hql, Long.class);
            return q.uniqueResultOptional().orElse(0L);
        }
    }
    
    public List<T> findWithQuery(String hql, int page, int size, Object... params) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<T> q = session.createQuery(hql, entityClass);
            for (int i = 0; i < params.length; i++) {
                q.setParameter(i + 1, params[i]);
            }
            q.setFirstResult(page * size);
            q.setMaxResults(size);
            return q.list();
        }
    }

}

