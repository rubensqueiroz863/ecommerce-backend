package com.rubens.ecommerce_backend.exception;

public class UserNotFoundException extends BusinessException {

    public UserNotFoundException() {
        super("USER_NOT_FOUND", "User not found.");
    }

    public UserNotFoundException(String message) {
        super("USER_NOT_FOUND", message);
    }
}