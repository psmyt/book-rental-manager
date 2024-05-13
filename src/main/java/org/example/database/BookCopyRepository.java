package org.example.database;

import org.example.entity.BookCopy;

import java.util.Optional;
import java.util.UUID;

public interface BookCopyRepository extends Repository<BookCopy> {
    Optional<BookCopy> findAvailableCopyOf(UUID bookId);
}
