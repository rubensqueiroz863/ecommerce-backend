package com.rubens.ecommerce_backend.dto;

import java.util.List;

public record UserRecommendationGroupDTO(
    String userId,
    String userName,
    List<ProductRecommendationDTO> recommendations
) {}