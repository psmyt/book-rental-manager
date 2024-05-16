package org.example.database;

import org.example.entity.BookCopy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface BookCopyRepository extends JpaRepository<BookCopy, UUID> {
    @Query("select c " +
            "from BookRental r " +
            "right join r.bookCopy c " +
            "where " +
            "c.book.id = :bookId and " +
            "(r is null or r.status in ('FINISHED', 'RESERVATION_EXPIRED') )")
    Optional<BookCopy> findAvailableCopyOf(UUID bookId);
}
