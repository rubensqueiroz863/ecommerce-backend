package com.rubens.ecommerce_backend.exception;

public class UserDeletionException extends BusinessException {

    public UserDeletionException() {
        super("COULD_NOT_DELETE_USER", "The user could not be deleted.");
    }

    public UserDeletionException(String message) {
        super("COULD_NOT_DELETE_USER", message);
    }
}