package com.rubens.ecommerce_backend.exception;

public class InvalidNameException extends BusinessException {

    public InvalidNameException() {
        super("INVALID_NAME", "Name is required.");
    }

    public InvalidNameException(String message) {
        super("INVALID_NAME", message);
    }
}