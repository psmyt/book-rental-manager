package org.example.service.impl;

import org.example.database.DatabaseAccess;
import org.example.dto.RentalView;
import org.example.entity.BookCopy;
import org.example.entity.BookRental;
import org.example.entity.Client;
import org.example.entity.RentalStatus;
import org.example.exception.AlreadyReservedException;
import org.example.exception.NoBookCopiesAvailableException;
import org.example.service.LockService;
import org.example.service.OtpService;
import org.example.service.RentalService;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class RentalServiceImpl implements RentalService {
    private final DatabaseAccess databaseAccess;
    private final LockService lockService;
    private final OtpService otpService;

    @Inject
    public RentalServiceImpl(DatabaseAccess databaseAccess, LockService lockService, OtpService otpService) {
        this.databaseAccess = databaseAccess;
        this.lockService = lockService;
        this.otpService = otpService;
    }

    @Override
    public RentalView reserve(UUID bookId, UUID clientId) throws NoBookCopiesAvailableException, AlreadyReservedException {
        Client client = databaseAccess.findClient(clientId);
        if (databaseAccess.alreadyReserved(bookId, clientId)) {
            throw new AlreadyReservedException();
        }
        lockService.lock(bookId);
        try {
            BookCopy bookCopy = databaseAccess.findAvailableBookCopy(bookId)
                    .orElseThrow(NoBookCopiesAvailableException::new);
            BookRental rental = databaseAccess.createRentalStatusReserved(bookCopy, client);
            return toView(rental);
        } finally {
            lockService.unlock(bookId);
        }
    }

    @Override
    public void deactivateExpiredReservations() {
        databaseAccess.deactivateExpiredReservations();
    }

    @Override
    public void sendOtp(UUID rentalId) {
        BookRental bookRental = databaseAccess.findRental(rentalId)
                .filter(rental -> RentalStatus.RESERVED.equals(rental.getStatus()))
                .orElseThrow(NotFoundException::new);
        otpService.sendOtp(bookRental.getId());
    }

    @Override
    public void validateOtp(UUID rentalId, String otp) {
        if (otpService.validateOtp(rentalId, otp)) {
            throw new BadRequestException();
        }
        BookRental bookRental = databaseAccess.findRental(rentalId)
                .filter(rental -> RentalStatus.RESERVED.equals(rental.getStatus()))
                .orElseThrow(NotFoundException::new);
        bookRental.setStatus(RentalStatus.ACTIVE);
        bookRental.setStartDate(LocalDate.now());
        bookRental.setEndDate(LocalDate.now().plusDays(14));
        databaseAccess.save(bookRental);
    }

    @Override
    public RentalView find(UUID rentalId) {
        BookRental bookRental = databaseAccess.findRental(rentalId)
                .orElseThrow(NotFoundException::new);
        return toView(bookRental);
    }

    @Override
    public void close(UUID rentalId) {
        BookRental bookRental = databaseAccess.findRental(rentalId)
                .orElseThrow(NotFoundException::new);
        bookRental.setStatus(RentalStatus.FINISHED);
        databaseAccess.save(bookRental);
    }

    @Override
    public List<RentalView> rentalHistory(UUID clientId, int page) {
        return databaseAccess.rentalHistory(clientId, page)
                .map(RentalServiceImpl::toView)
                .collect(Collectors.toList());
    }

    private static RentalView toView(BookRental rental) {
        return RentalView.builder()
                .rentalId(rental.getId())
                .bookId(rental.getBookCopy().getBook().getId())
                .bookCopyId(rental.getBookCopy().getId())
                .status(rental.getStatus())
                .build();
    }
}
