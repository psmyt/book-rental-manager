package org.example.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(
        value = HttpStatus.BAD_REQUEST,
        reason = "неверный код отп")
public class OtpValidationException extends RuntimeException {
}
