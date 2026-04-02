package com.rubens.ecommerce_backend.exception;

public class InvalidSubCategoryException extends BusinessException {

    public InvalidSubCategoryException() {
        super("INVALID_SUB_CATEGORY", "Invalid SubCategory.");
    }

    public InvalidSubCategoryException(String message) {
        super("INVALID_SUB_CATEGORY", message);
    }
}