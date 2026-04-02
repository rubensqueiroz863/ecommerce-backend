package com.rubens.ecommerce_backend.exception;

public class InvalidProductPriceException extends BusinessException {

    public InvalidProductPriceException() {
        super("INVALID_PRODUCT_PRICE", "Invalid product price.");
    }

    public InvalidProductPriceException(String message) {
        super("INVALID_PRODUCT_PRICE", message);
    }
}