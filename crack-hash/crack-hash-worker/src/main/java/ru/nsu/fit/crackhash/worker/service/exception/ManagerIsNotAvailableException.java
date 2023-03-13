package ru.nsu.fit.crackhash.worker.service.exception;

public class ManagerIsNotAvailableException extends RuntimeException {
    public ManagerIsNotAvailableException() {
        super("Manager is not available");
    }
}
