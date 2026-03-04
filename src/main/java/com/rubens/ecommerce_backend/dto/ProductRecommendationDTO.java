package com.rubens.ecommerce_backend.dto;

public record ProductRecommendationDTO(
    String productId,
    String productName,
    Double productPrice,
    Long usersInCommon
) {}