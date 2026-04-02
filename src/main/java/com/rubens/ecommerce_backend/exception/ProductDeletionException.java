package com.rubens.ecommerce_backend.exception;

public class ProductDeletionException extends BusinessException {

    public ProductDeletionException() {
        super("COULD_NOT_DELETE_PRODUCT", "The product could not be deleted.");
    }

    public ProductDeletionException(String message) {
        super("COULD_NOT_DELETE_PRODUCT", message);
    }
}