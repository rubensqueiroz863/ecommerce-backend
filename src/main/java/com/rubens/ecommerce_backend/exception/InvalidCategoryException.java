package com.rubens.ecommerce_backend.exception;

public class InvalidCategoryException extends BusinessException {

    public InvalidCategoryException() {
        super("INVALID_CATEGORY", "Invalid category.");
    }

    public InvalidCategoryException(String message) {
        super("INVALID_CATEGORY", message);
    }
}