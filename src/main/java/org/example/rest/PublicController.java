package org.example.rest;

import org.example.dto.BookDto;
import org.example.dto.RentalView;
import org.example.exception.AlreadyReservedException;
import org.example.service.BookService;
import org.example.exception.NoBookCopiesAvailableException;
import org.example.service.RentalService;
import org.keycloak.KeycloakPrincipal;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Path("/public")
public class PublicController {
    @Inject
    private BookService bookService;
    @Inject
    private RentalService rentalService;

    @GET
    @Path("/books")
    @Produces(MediaType.APPLICATION_JSON)
    public List<BookDto> getBooks() {
        return bookService.getBooks();
    }

    @POST
    @RolesAllowed("client")
    @Path("/books/{bookId}/reserve")
    @Produces(MediaType.APPLICATION_JSON)
    public RentalView reserve(@PathParam("bookId") UUID bookId, @Context SecurityContext securityContext)
            throws AlreadyReservedException, NoBookCopiesAvailableException {
        UUID clientId = getClientId(securityContext);
        return rentalService.reserve(bookId, clientId);
    }

    @GET
    @RolesAllowed("client")
    @Path("/rentals/history")
    @Produces(MediaType.APPLICATION_JSON)
    public List<RentalView> history(@QueryParam("page") int page, @Context SecurityContext securityContext) {
        if (page < 0) {
            throw new IllegalArgumentException();
        }
        UUID clientId = getClientId(securityContext);
        return rentalService.rentalHistory(clientId, page);
    }

    // TODO вынести в фильтр
    private static UUID getClientId(SecurityContext securityContext) {
        Object clientId = ((KeycloakPrincipal<?>) securityContext.getUserPrincipal())
                .getKeycloakSecurityContext()
                .getIdToken()
                .getOtherClaims()
                .get("clientId");
        return UUID.fromString((String) Objects.requireNonNull(clientId));
    }
}
