package org.example.exceptions;

public class UserNotRegistred extends RuntimeException {
    public UserNotRegistred(String message) {
        super(message);
    }
}
