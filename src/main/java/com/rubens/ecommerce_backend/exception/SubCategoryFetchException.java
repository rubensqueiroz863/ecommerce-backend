package com.rubens.ecommerce_backend.exception;

public class SubCategoryFetchException extends BusinessException {

    public SubCategoryFetchException() {
        super("SUB_CATEGORY_FETCH", "SubCategories could not be fetched.");
    }

    public SubCategoryFetchException(String message) {
        super("SUB_CATEGORY_FETCH", message);
    }
}