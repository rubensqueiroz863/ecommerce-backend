package com.rubens.ecommerce_backend.exception;

public class InvalidPageException extends BusinessException {

    public InvalidPageException() {
        super("INVALID_PAGE", "Invalid page.");
    }

    public InvalidPageException(String message) {
        super("INVALID_PAGE", message);
    }
}