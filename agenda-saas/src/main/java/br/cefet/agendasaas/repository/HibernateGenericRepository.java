package br.cefet.agendasaas.repository;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import br.cefet.agendasaas.dao.GenericDAO;

public class HibernateGenericRepository<T, ID extends Serializable> implements GenericRepository<T, ID> {

    private final GenericDAO<T, ID> dao;

    public HibernateGenericRepository(Class<T> entityClass) {
        this.dao = new GenericDAO<>(entityClass);
    }

    @Override
    public Optional<T> findById(ID id) {
        return dao.findById(id);
    }

    @Override
    public List<T> findAll() {
        return dao.findAll();
    }

    @Override
    public List<T> findAll(int page, int size) {
        return dao.findAll(page, size);
    }

    @Override
    public long count() {
        return dao.count();
    }

    @Override
    public T save(T entity) {
        return dao.save(entity);
    }

    @Override
    public T update(T entity) {
        return dao.update(entity);
    }

    @Override
    public void delete(T entity) {
        dao.delete(entity);
    }

    @Override
    public int deleteById(ID id) {
        return dao.deleteById(id);
    }

    @Override
    public List<T> findWithQuery(String hql, Object... params) {
        return dao.findWithQuery(hql, params);
    }

    @Override
    public List<T> findWithQuery(String hql, int page, int size, Object... params) {
        return dao.findWithQuery(hql, page, size, params);
    }

}

