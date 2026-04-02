package com.rubens.ecommerce_backend.exception;

public class SearchCreationException extends BusinessException {

    public SearchCreationException() {
        super("SEARCH_CREATION", "Search cannot be created.");
    }

    public SearchCreationException(String message) {
        super("SEARCH_CREATION", message);
    }
}