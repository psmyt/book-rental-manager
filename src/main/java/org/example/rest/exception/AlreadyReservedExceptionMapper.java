package org.example.rest.exception;

import org.example.exception.AlreadyReservedException;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class AlreadyReservedExceptionMapper
        implements ExceptionMapper<AlreadyReservedException> {
    @Override
    public Response toResponse(AlreadyReservedException e) {
        return Response.status(400)
                .type(MediaType.APPLICATION_JSON_TYPE)
                .entity(new BadRequestResponse(
                        "alreadyReserved",
                        "у вас уже есть действующая бронь на эту книгу"
                ))
                .build();
    }
}
