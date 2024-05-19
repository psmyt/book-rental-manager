package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.config.Configuration;
import org.example.database.BookCopyRepository;
import org.example.database.ClientRepository;
import org.example.database.RentalRepository;
import org.example.exception.*;
import org.example.service.LockService;
import org.example.service.OtpService;
import org.example.service.RentalService;
import org.example.dto.RentalView;
import org.example.entity.BookCopy;
import org.example.entity.BookRental;
import org.example.entity.Client;
import org.example.entity.RentalStatus;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.example.entity.RentalStatus.*;

@Service
@RequiredArgsConstructor
public class RentalServiceImpl implements RentalService {
    private final RentalRepository rentalRepository;
    private final ClientRepository clientRepository;
    private final BookCopyRepository bookCopyRepository;
    private final LockService lockService;
    private final OtpService otpService;
    private final Configuration configuration;

    @Override
    public RentalView reserve(UUID bookId, UUID clientId) throws NoBookCopiesAvailableException, AlreadyReservedException {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(NotFoundException::new);
        if (!rentalRepository.findByBookIdAndClientIdAndStatusIn(bookId, clientId, ACTIVE, RESERVED).isEmpty()) {
            throw new AlreadyReservedException();
        }
        lockService.lock(bookId);
        try {
            BookCopy bookCopy = bookCopyRepository.findAvailableCopyOf(bookId)
                    .orElseThrow(NoBookCopiesAvailableException::new);
            BookRental rental = BookRental.builder()
                    .status(RESERVED)
                    .bookCopy(bookCopy)
                    .client(client)
                    .build();
            rentalRepository.save(rental);
            return toView(rental);
        } finally {
            lockService.unlock(bookId);
        }
    }

    @Override
    public void deactivateExpiredReservations() {
        rentalRepository.deactivateAfter(
                Instant.now().minus(configuration.getReservationTtl())
        );
    }

    @Override
    public void sendOtp(UUID rentalId) {
        BookRental bookRental = rentalRepository.findById(rentalId)
                .filter(rental -> RESERVED.equals(rental.getStatus()))
                .orElseThrow(NotFoundException::new);
        otpService.sendOtp(bookRental.getId());
    }

    @Override
    public void validateOtp(UUID rentalId, String otp) {
        if (!otpService.validateOtp(rentalId, otp)) {
            throw new OtpValidationException();
        }
        BookRental bookRental = rentalRepository.findById(rentalId)
                .filter(rental -> RESERVED.equals(rental.getStatus()))
                .orElseThrow(NotFoundException::new);
        bookRental.setStatus(ACTIVE);
        bookRental.setStartDate(LocalDate.now());
        bookRental.setEndDate(LocalDate.now().plusDays(14));
        rentalRepository.save(bookRental);
    }

    @Override
    public RentalView find(UUID rentalId) {
        BookRental bookRental = rentalRepository.findById(rentalId)
                .orElseThrow(NotFoundException::new);
        return toView(bookRental);
    }

    @Override
    public void close(UUID rentalId) {
        BookRental bookRental = rentalRepository.findById(rentalId)
                .orElseThrow(NotFoundException::new);
        bookRental.setStatus(RentalStatus.FINISHED);
        rentalRepository.save(bookRental);
    }

    @Override
    public List<RentalView> rentalHistory(UUID clientId, int page) {
        return rentalRepository.findByClientIdAndStatusNotInOrderByCreatedDesc(
                        clientId,
                        PageRequest.of(page, 10),
                        RESERVATION_EXPIRED)
                .map(RentalServiceImpl::toView)
                .toList();
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
