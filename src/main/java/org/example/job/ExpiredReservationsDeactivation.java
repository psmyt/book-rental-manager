package org.example.job;

import org.example.service.RentalService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class ExpiredReservationsDeactivation {
    private final RentalService rentalService;

    public ExpiredReservationsDeactivation(RentalService rentalService) {
        this.rentalService = rentalService;
    }

    @Scheduled(fixedRate = 1, timeUnit = TimeUnit.MINUTES)
    void deactivate() {
        rentalService.deactivateExpiredReservations();
    }
}
