package com.rubens.ecommerce_backend.exception;

public class InvalidProductIdException extends BusinessException {

    public InvalidProductIdException() {
        super("INVALID_PRODUCT_ID", "Invalid productId.");
    }

    public InvalidProductIdException(String message) {
        super("INVALID_PRODUCT_ID", message);
    }
}