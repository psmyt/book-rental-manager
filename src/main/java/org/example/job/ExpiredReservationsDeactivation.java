package org.example.job;

import lombok.AllArgsConstructor;
import org.example.service.RentalService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@AllArgsConstructor
public class ExpiredReservationsDeactivation {
    private final RentalService rentalService;

    @Scheduled(fixedRate = 1, timeUnit = TimeUnit.MINUTES)
    void deactivate() {
        rentalService.deactivateExpiredReservations();
    }
}
