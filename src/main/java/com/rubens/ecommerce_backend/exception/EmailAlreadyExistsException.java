package com.rubens.ecommerce_backend.exception;

public class EmailAlreadyExistsException extends BusinessException {

    public EmailAlreadyExistsException() {
        super(
            "EMAIL_ALREADY_EXISTS",
            "That email address is taken. Try another."
        );
    }

    public EmailAlreadyExistsException(String message) {
        super(
            "EMAIL_ALREADY_EXISTS",
            message
        );
    }
}