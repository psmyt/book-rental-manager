package org.example.service.impl;

import org.example.database.BookCopyRepository;
import org.example.database.ClientRepository;
import org.example.database.RentalRepository;
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

import static org.example.entity.RentalStatus.ACTIVE;
import static org.example.entity.RentalStatus.RESERVED;

public class RentalServiceImpl implements RentalService {
    private final RentalRepository rentalRepository;
    private final ClientRepository clientRepository;
    private final BookCopyRepository bookCopyRepository;
    private final LockService lockService;
    private final OtpService otpService;

    @Inject
    public RentalServiceImpl(RentalRepository rentalRepository,
                             ClientRepository clientRepository,
                             BookCopyRepository bookCopyRepository, LockService lockService,
                             OtpService otpService
    ) {
        this.rentalRepository = rentalRepository;
        this.clientRepository = clientRepository;
        this.bookCopyRepository = bookCopyRepository;
        this.lockService = lockService;
        this.otpService = otpService;
    }

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
        rentalRepository.deactivateExpired();
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
        if (otpService.validateOtp(rentalId, otp)) {
            throw new BadRequestException();
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
        return rentalRepository.history(clientId, page)
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
