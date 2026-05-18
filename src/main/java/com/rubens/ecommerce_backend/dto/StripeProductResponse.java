package com.rubens.ecommerce_backend.dto;

public record StripeProductResponse(
    String productId,
    String priceId
) {}