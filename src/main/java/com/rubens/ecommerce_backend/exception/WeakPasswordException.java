package com.rubens.ecommerce_backend.exception;

public class WeakPasswordException extends BusinessException {

    public WeakPasswordException() {
        super("WEAK_PASSWORD", "Password is too weak.");
    }

    public WeakPasswordException(String message) {
        super("WEAK_PASSWORD", message);
    }
}