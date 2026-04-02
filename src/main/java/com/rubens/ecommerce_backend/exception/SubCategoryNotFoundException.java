package com.rubens.ecommerce_backend.exception;

public class SubCategoryNotFoundException extends BusinessException {

    public SubCategoryNotFoundException() {
        super("SUB_CATEGORY_NOT_FOUND", "SubCategory not found.");
    }

    public SubCategoryNotFoundException(String message) {
        super("SUB_CATEGORY_NOT_FOUND", message);
    }
}