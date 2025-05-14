package com.example.searchservice.core.exception;

public class DatabaseConflictException extends RuntimeException {
    public DatabaseConflictException(String message, Throwable cause) {
        super(message, cause);
    }

    public DatabaseConflictException(String message) {
        super(message);
    }
}
