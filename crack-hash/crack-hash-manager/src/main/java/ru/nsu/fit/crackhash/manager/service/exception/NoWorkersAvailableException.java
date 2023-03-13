package ru.nsu.fit.crackhash.manager.service.exception;

public class NoWorkersAvailableException extends RuntimeException {
    public NoWorkersAvailableException() {
        super("No workers available");
    }
}
