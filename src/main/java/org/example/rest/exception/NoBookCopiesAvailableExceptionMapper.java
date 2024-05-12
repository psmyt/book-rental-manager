package org.example.rest.exception;

import org.example.exception.NoBookCopiesAvailableException;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class NoBookCopiesAvailableExceptionMapper
        implements ExceptionMapper<NoBookCopiesAvailableException> {
    @Override
    public Response toResponse(NoBookCopiesAvailableException exception) {
        return Response.status(400)
                .type(MediaType.APPLICATION_JSON_TYPE)
                .entity(new BadRequestResponse(
                        "noBookCopiesAvailable",
                        "нет свободных книг"
                ))
                .build();
    }
}
