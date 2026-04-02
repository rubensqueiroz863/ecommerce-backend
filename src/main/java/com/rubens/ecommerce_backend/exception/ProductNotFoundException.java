package com.rubens.ecommerce_backend.exception;

public class ProductNotFoundException extends BusinessException {

    public ProductNotFoundException() {
        super("PRODUCT_NOT_FOUND", "Product not found.");
    }

    public ProductNotFoundException(String message) {
        super("PRODUCT_NOT_FOUND", message);
    }
}