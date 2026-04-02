package com.rubens.ecommerce_backend.exception;

public class InvalidProductNameException extends BusinessException {

    public InvalidProductNameException() {
        super("INVALID_PRODUCT_NAME", "Invalid product name.");
    }

    public InvalidProductNameException(String message) {
        super("INVALID_PRODUCT_NAME", message);
    }
}