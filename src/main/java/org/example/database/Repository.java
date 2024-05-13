package org.example.database;

import java.util.Optional;
import java.util.UUID;

public interface Repository<T> {
    Optional<T> findById(UUID id);
    T save(T entity);
}
