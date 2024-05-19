package org.example.service.impl;

import org.example.config.Configuration;
import org.example.database.BookCopyRepository;
import org.example.database.ClientRepository;
import org.example.database.RentalRepository;
import org.example.entity.Book;
import org.example.entity.BookCopy;
import org.example.entity.BookRental;
import org.example.entity.Client;
import org.example.exception.AlreadyReservedException;
import org.example.exception.NoBookCopiesAvailableException;
import org.example.service.LockService;
import org.example.service.OtpService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import test_data.Random;
import test_data.RentalServiceParameterResolver;

import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

import static org.mockito.Mockito.*;

@ExtendWith({RentalServiceParameterResolver.class, MockitoExtension.class})
class RentalServiceImplTest {
    @Mock
    RentalRepository rentalRepository;
    @Mock
    ClientRepository clientRepository;
    @Mock
    BookCopyRepository bookCopyRepository;
    @Mock
    Configuration configuration;
    @Spy
    LockService lockService = new PrimitiveLockService();
    @Spy
    OtpService otpService = new OtpServiceMock();

    @InjectMocks
    private RentalServiceImpl rentalService;

    @Test
    void create_rental_locking_test(BookRental bookRental,
                                    Book book,
                                    BookCopy bookCopy,
                                    Client client,
                                    @Random Client randomClient) throws InterruptedException {
        AtomicInteger counter = new AtomicInteger(0);

        doReturn(Optional.of(client))
                .when(clientRepository)
                .findById(client.getId());

        doReturn(Optional.of(randomClient))
                .when(clientRepository)
                .findById(randomClient.getId());

        doReturn(Collections.emptyList())
                .when(rentalRepository)
                .findByBookIdAndClientIdAndStatusIn(any(), any(), any());

        when(bookCopyRepository.findAvailableCopyOf(any()))
                .thenAnswer(i -> Optional.of(bookCopy));

        when(rentalRepository.save(any()))
                .thenAnswer(i -> {
                    counter.incrementAndGet();
                    Thread.sleep(5000);
                    return bookRental;
                }).thenAnswer(i -> {
                    counter.incrementAndGet();
                    return bookRental;
                });

        //будет выполняться 5 секунд
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            try {
                rentalService.reserve(book.getId(), client.getId());
            } catch (NoBookCopiesAvailableException | AlreadyReservedException e) {
                throw new RuntimeException(e);
            }
        });

        Thread.sleep(1000);

        CompletableFuture<Void> future2 = CompletableFuture.runAsync(() -> {
            try {
                rentalService.reserve(book.getId(), randomClient.getId());
            } catch (NoBookCopiesAvailableException | AlreadyReservedException e) {
                throw new RuntimeException(e);
            }
        });

        Thread.sleep(1000);

        Assertions.assertEquals(1, counter.get());

        future.join();
        future2.join();
    }
}