package org.example.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(
        value = HttpStatus.BAD_REQUEST,
        reason = "нет свободных копий")
public class NoBookCopiesAvailableException extends Exception {
}
