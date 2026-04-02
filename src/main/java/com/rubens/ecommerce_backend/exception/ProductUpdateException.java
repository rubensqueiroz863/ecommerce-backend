package com.rubens.ecommerce_backend.exception;

public class ProductUpdateException extends BusinessException {

    public ProductUpdateException() {
        super("COULD_NOT_UPDATE_PRODUCT", "Product could not be updated.");
    }

    public ProductUpdateException(String message) {
        super("COULD_NOT_UPDATE_PRODUCT", message);
    }
}