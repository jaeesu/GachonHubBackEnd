package com.example.gachonhub.exception;

public class NotAccessUserException extends RuntimeException {

    public NotAccessUserException() {
        super();
    }

    public NotAccessUserException(String message) {
        super(message);
    }
}
