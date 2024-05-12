package org.example.rest;

import org.example.dto.RentalView;
import org.example.service.RentalService;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.*;
import java.util.UUID;

@Path("/private/rentals")
@RolesAllowed("employee")
public class PrivateController {

    @Inject
    private RentalService rentalService;

    @GET
    @Path("/{rentalId}")
    public RentalView getRental(@PathParam("rentalId") UUID rentalId) {
        return rentalService.find(rentalId);
    }

    @POST
    @Path("/{rentalId}/sendOtp")
    public void sendOtp(@PathParam("rentalId") UUID rentalId) {
        rentalService.sendOtp(rentalId);
    }

    @POST
    @Path("/{rentalId}/confirm")
    public void validateOtp(@PathParam("rentalId") UUID rentalId, @QueryParam("otp") String otp) {
        rentalService.validateOtp(rentalId, otp);
    }

    @POST
    @Path("/{rentalId}/close")
    public void closeRental(@PathParam("rentalId") UUID rentalId) {
        rentalService.close(rentalId);
    }
}
