package com.rubens.ecommerce_backend.exception;

public record ErrorResponse(
    String code,
    String message
) {}