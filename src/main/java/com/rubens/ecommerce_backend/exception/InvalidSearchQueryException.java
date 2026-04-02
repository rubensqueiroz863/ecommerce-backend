package com.rubens.ecommerce_backend.exception;

public class InvalidSearchQueryException extends BusinessException {

    public InvalidSearchQueryException() {
        super("INVALID_SEARCH_QUERY", "Invalid search query.");
    }

    public InvalidSearchQueryException(String message) {
        super("INVALID_SEARCH_QUERY", message);
    }
}