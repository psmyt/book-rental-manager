package org.example.service;

import org.example.dto.RentalView;
import org.example.exception.AlreadyReservedException;
import org.example.exception.NoBookCopiesAvailableException;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

public interface RentalService {
    RentalView reserve(UUID bookId, UUID clientId) throws
            NoBookCopiesAvailableException,
            AlreadyReservedException;

    void deactivateExpiredReservations();

    void sendOtp(UUID rentalId);

    void validateOtp(UUID rentalId, String otp);

    RentalView find(UUID rentalId);

    void close(UUID rentalId);

    List<RentalView> rentalHistory(UUID clientId, int page);
}
