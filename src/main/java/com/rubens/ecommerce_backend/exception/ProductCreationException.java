package com.rubens.ecommerce_backend.exception;

public class ProductCreationException extends BusinessException {

    public ProductCreationException() {
        super("PRODUCT_CREATION", "Product cannot be created.");
    }

    public ProductCreationException(String message) {
        super("PRODUCT_CREATION", message);
    }
}