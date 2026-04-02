package com.rubens.ecommerce_backend.exception;

public class InvalidLimitException extends BusinessException {

    public InvalidLimitException() {
        super("INVALID_LIMIT", "Invalid limit.");
    }

    public InvalidLimitException(String message) {
        super("INVALID_LIMIT", message);
    }
}