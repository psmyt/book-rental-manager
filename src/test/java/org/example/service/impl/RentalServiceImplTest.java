package org.example.service.impl;

import org.example.database.DatabaseAccess;
import org.example.dto.RentalView;
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
import test_data.Random;
import test_data.RentalServiceParameterResolver;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

import static org.mockito.Mockito.*;

@ExtendWith(RentalServiceParameterResolver.class)
class RentalServiceImplTest {

    DatabaseAccess databaseAccess = mock(DatabaseAccess.class);
    LockService lockService = new PrimitiveLockService();
    OtpService otpService = new OtpServiceMock();

    private final RentalServiceImpl rentalService = new RentalServiceImpl(
            databaseAccess, lockService, otpService
    );

    @Test
    void create_rental_locking_test(BookRental bookRental,
                                    Book book,
                                    BookCopy bookCopy,
                                    Client client,
                                    @Random Client randomClient) throws InterruptedException {
        AtomicInteger counter = new AtomicInteger(0);

        doReturn(client)
                .when(databaseAccess)
                .findClient(client.getId());

        doReturn(randomClient)
                .when(databaseAccess)
                .findClient(randomClient.getId());

        when(databaseAccess.alreadyReserved(any(), any()))
                .thenReturn(false);

        doAnswer(invocation -> {
            counter.incrementAndGet();
            Client actualClient = invocation.getArgument(1);
            Assertions.assertEquals(client, actualClient);
            return bookRental;
        }).when(databaseAccess)
                .createRentalStatusReserved(any(), any());

        when(databaseAccess.findAvailableBookCopy(any()))
                .thenAnswer(i -> {
                    Thread.sleep(5000);
                    return Optional.of(bookCopy);
                })
                .thenAnswer(i -> Optional.empty());

        //будет выполняться 5 секунд
        CompletableFuture<RentalView> future = CompletableFuture.supplyAsync(() -> {
            try {
                return rentalService.reserve(book.getId(), client.getId());
            } catch (NoBookCopiesAvailableException | AlreadyReservedException e) {
                throw new RuntimeException(e);
            }
        });

        Thread.sleep(1000);

        boolean completedExceptionally = CompletableFuture.runAsync(() -> Assertions.assertThrows(
                NoBookCopiesAvailableException.class,
                () -> rentalService.reserve(book.getId(), randomClient.getId()))
        ).isCompletedExceptionally();

        if (completedExceptionally)
            throw new AssertionError();

        future.join();
        Assertions.assertEquals(1, counter.get());
    }
}