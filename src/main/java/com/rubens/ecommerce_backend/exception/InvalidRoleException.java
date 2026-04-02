package com.rubens.ecommerce_backend.exception;

public class InvalidRoleException extends BusinessException {

    public InvalidRoleException() {
        super("INVALID_ROLE", "Invalid role format.");
    }

    public InvalidRoleException(String message) {
        super("INVALID_ROLE", message);
    }
}