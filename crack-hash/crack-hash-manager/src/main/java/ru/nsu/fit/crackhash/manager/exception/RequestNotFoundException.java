package ru.nsu.fit.crackhash.manager.exception;

import java.util.UUID;

public class RequestNotFoundException extends RuntimeException {
    public RequestNotFoundException(UUID requestId) {
        super(String.format("Request with ID %s not found", requestId));
    }
}
