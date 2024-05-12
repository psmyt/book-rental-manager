package org.example.job;

import org.example.service.RentalService;

import javax.inject.Inject;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ExpiredReservationsDeactivation {
    private final ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

    @Inject
    void init(RentalService rentalService) {
        scheduledExecutorService.scheduleAtFixedRate(
                rentalService::deactivateExpiredReservations,
                0,
                1,
                TimeUnit.MINUTES
        );
    }
}
