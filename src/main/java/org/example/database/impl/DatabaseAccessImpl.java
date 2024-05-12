package org.example.database.impl;

import org.example.database.DatabaseAccess;
import org.example.database.impl.repository.BookCopyRepository;
import org.example.database.impl.repository.BookRepository;
import org.example.database.impl.repository.ClientRepository;
import org.example.database.impl.repository.RentalRepository;
import org.example.entity.*;

import javax.inject.Inject;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

public class DatabaseAccessImpl implements DatabaseAccess {
    @Inject
    private BookRepository bookRepository;
    @Inject
    private RentalRepository rentalRepository;
    @Inject
    private ClientRepository cLientRepository;
    @Inject
    private BookCopyRepository bookCopyRepository;

    @Override
    public List<Book> allBooks() {
        return bookRepository.allBooks();
    }

    @Override
    public boolean alreadyReserved(UUID bookId, UUID clientId) {
        return !rentalRepository.findByBookIdAndClientIdAndStatusIn(
                bookId, clientId, RentalStatus.ACTIVE, RentalStatus.RESERVED
        ).isEmpty();
    }

    @Override
    public Client findClient(UUID clientId) {
        return cLientRepository.findById(clientId)
                .orElseThrow(NoSuchElementException::new);
    }

    @Override
    public BookRental createRentalStatusReserved(BookCopy bookCopy, Client client) {
        return rentalRepository.save(
                BookRental.builder()
                        .status(RentalStatus.RESERVED)
                        .bookCopy(bookCopy)
                        .client(client)
                        .build()
        );
    }

    @Override
    public Optional<BookCopy> findAvailableBookCopy(UUID bookId) {
        return bookCopyRepository.findAvailableCopyOf(bookId);
    }

    @Override
    public void deactivateExpiredReservations() {
        rentalRepository.deactivateExpired();
    }

    @Override
    public Optional<BookRental> findRental(UUID rentalId) {
        return rentalRepository.findById(rentalId);
    }

    @Override
    public void save(BookRental rental) {
        rentalRepository.save(rental);
    }

    @Override
    public Stream<BookRental> rentalHistory(UUID clientId, int page) {
        return rentalRepository.history(clientId, page);
    }
}
