package com.rubens.ecommerce_backend.dto;

public record ClickRequestDTO(
    String productId,
    String userEmail
) {}