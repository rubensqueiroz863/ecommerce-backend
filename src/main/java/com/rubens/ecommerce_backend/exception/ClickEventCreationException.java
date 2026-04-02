package com.rubens.ecommerce_backend.exception;

public class ClickEventCreationException extends BusinessException {

    public ClickEventCreationException() {
        super("CLICK_CREATION", "Click cannot be created.");
    }

    public ClickEventCreationException(String message) {
        super("CLICK_CREATION", message);
    }
}