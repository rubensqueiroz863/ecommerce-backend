package com.rubens.ecommerce_backend.dto;

public record ProductRecommendationDTO(
    String productId,
    String productName,
    Long usersInCommon
) {}