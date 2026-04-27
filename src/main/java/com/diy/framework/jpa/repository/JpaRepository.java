package com.diy.framework.jpa.repository;

import java.util.Collection;
import java.util.Optional;

public interface JpaRepository<T, ID> {

    Optional<T> findById(ID id);

    Collection<T> findAll();

    T save(T o);

    ID delete(T o);

}
