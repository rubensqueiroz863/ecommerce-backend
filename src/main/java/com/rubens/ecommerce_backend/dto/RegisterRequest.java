package com.rubens.ecommerce_backend.dto;

public record RegisterRequest(
    String name,
    String email,
    String password
) {}