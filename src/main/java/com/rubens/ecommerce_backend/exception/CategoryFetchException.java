package com.rubens.ecommerce_backend.exception;

public class CategoryFetchException extends BusinessException {

    public CategoryFetchException() {
        super("CATEGORY_FETCH", "Categories could not be fetched.");
    }

    public CategoryFetchException(String message) {
        super("CATEGORY_FETCH", message);
    }
}