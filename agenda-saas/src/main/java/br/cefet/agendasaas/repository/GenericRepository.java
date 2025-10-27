package br.cefet.agendasaas.repository;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface GenericRepository<T, ID extends Serializable> {

    Optional<T> findById(ID id);

    List<T> findAll();

    List<T> findAll(int page, int size);

    long count();

    T save(T entity);

    T update(T entity);

    void delete(T entity);

    int deleteById(ID id);

    List<T> findWithQuery(String hql, Object... params);

    List<T> findWithQuery(String hql, int page, int size, Object... params);

}
