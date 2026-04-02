package com.rubens.ecommerce_backend.exception;

public class InvalidEmailException extends BusinessException {

    public InvalidEmailException() {
        super("INVALID_EMAIL", "Invalid email format.");
    }

    public InvalidEmailException(String message) {
        super("INVALID_EMAIL", message);
    }
}