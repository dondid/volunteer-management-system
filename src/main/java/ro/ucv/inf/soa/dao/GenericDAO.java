package ro.ucv.inf.soa.dao;

import java.util.List;
import java.util.Optional;

public interface GenericDAO<T, ID> {

    // CREATE
    T save(T entity);

    // READ
    Optional<T> findById(ID id);
    List<T> findAll();
    long count();

    // UPDATE
    T update(T entity);

    // DELETE
    void delete(T entity);
    void deleteById(ID id);
    boolean existsById(ID id);
}