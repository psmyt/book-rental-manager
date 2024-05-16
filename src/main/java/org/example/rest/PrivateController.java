package org.example.rest;

import lombok.AllArgsConstructor;
import org.example.dto.RentalView;
import org.example.service.RentalService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.UUID;

@RequestMapping("/private/rentals")
@RolesAllowed("employee")
@RestController
@AllArgsConstructor
public class PrivateController {

    private final RentalService rentalService;

    @PostMapping("/{rentalId}")
    public RentalView getRental(@PathVariable("rentalId") UUID rentalId) {
        return rentalService.find(rentalId);
    }

    @PostMapping("/{rentalId}/sendOtp")
    public void sendOtp(@PathVariable("rentalId") UUID rentalId) {
        rentalService.sendOtp(rentalId);
    }

    @PostMapping("/{rentalId}/confirm")
    public void validateOtp(@PathVariable("rentalId") UUID rentalId, @RequestParam("otp") String otp) {
        rentalService.validateOtp(rentalId, otp);
    }

    @PostMapping("/{rentalId}/close")
    public void closeRental(@PathVariable("rentalId") UUID rentalId) {
        rentalService.close(rentalId);
    }
}
