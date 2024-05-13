package org.example.database.impl;

import org.example.database.BookCopyRepository;
import org.example.entity.BookCopy;

import java.util.Optional;
import java.util.UUID;

public class BookCopyRepositoryImpl extends BaseRepository<BookCopy> implements BookCopyRepository {
    String AVAILABLE_COPY = "select c " +
            "from BookRental r " +
            "right join r.bookCopy c " +
            "where " +
            "c.book.id = :bookId and " +
            "(r is null or r.status in ('FINISHED', 'RESERVATION_EXPIRED') )";

    @Override
    public Optional<BookCopy> findAvailableCopyOf(UUID bookId) {
        return tryWithNewSession(
                session -> session.createQuery(AVAILABLE_COPY, BookCopy.class)
                        .setParameter("bookId", bookId)
                        .stream()
                        .findAny()
        );
    }
}
