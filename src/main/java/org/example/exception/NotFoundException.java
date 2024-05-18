package org.example.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(
        value = HttpStatus.NOT_FOUND,
        reason = "экземпляр не найден")
public class NotFoundException extends RuntimeException {
}
