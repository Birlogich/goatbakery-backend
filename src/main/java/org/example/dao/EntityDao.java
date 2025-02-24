package org.example.dao;

import java.util.Optional;

public interface EntityDao<T, K, E> {

    T save(T t);

    Optional<T> findById(E e);


    boolean delete(E e);

    boolean isPresent(E e);

    T edit(T t, K k);
}
