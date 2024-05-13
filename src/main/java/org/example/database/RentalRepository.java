package org.example.database;

import org.example.entity.BookRental;
import org.example.entity.RentalStatus;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

public interface RentalRepository extends Repository<BookRental> {
    List<BookRental> findByBookIdAndClientIdAndStatusIn(UUID bookId, UUID clientId, RentalStatus... statuses);
    void deactivateExpired();
    Stream<BookRental> history(UUID clientId, int page);
}
