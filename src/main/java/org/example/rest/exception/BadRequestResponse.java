package org.example.rest.exception;

public class BadRequestResponse {
    final String exceptionCode;
    final String messageToClient;

    public BadRequestResponse(String exceptionCode, String messageToClient) {
        this.exceptionCode = exceptionCode;
        this.messageToClient = messageToClient;
    }
}
