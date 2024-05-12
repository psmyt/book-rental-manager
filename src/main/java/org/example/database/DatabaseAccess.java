package org.example.database;

import org.example.entity.Book;
import org.example.entity.BookCopy;
import org.example.entity.BookRental;
import org.example.entity.Client;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

public interface DatabaseAccess {
    List<Book> allBooks();

    Optional<BookCopy> findAvailableBookCopy(UUID bookId);

    boolean alreadyReserved(UUID bookId, UUID clientId);

    Client findClient(UUID clientId);

    BookRental createRentalStatusReserved(BookCopy bookCopy, Client client);

    void deactivateExpiredReservations();

    Optional<BookRental> findRental(UUID rentalId);

    void save(BookRental rental);

    Stream<BookRental> rentalHistory(UUID clientId, int page);
}
