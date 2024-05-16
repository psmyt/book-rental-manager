package org.example.database;

import org.example.entity.BookRental;
import org.example.entity.RentalStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface RentalRepository extends JpaRepository<BookRental, UUID> {
    @Query("SELECT from BookRental " +
            "WHERE " +
            "client.id = :clientId and " +
            "status in ( :statuses ) and " +
            "bookCopy.book.id = :bookId")
    List<BookRental> findByBookIdAndClientIdAndStatusIn(UUID bookId, UUID clientId, RentalStatus... statuses);

    @Query("UPDATE BookRental " +
            "set status = 'RESERVATION_EXPIRED' " +
            "WHERE " +
            "status = 'RESERVED' " +
            "and created + 10 * interval '1 minute' < now()")
    void deactivateExpired();

    Page<BookRental> findByClientIdAndStatusNotInOrderByCreatedDesc(UUID clientId, RentalStatus status, Pageable pageable);
}
