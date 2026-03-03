package com.rubens.ecommerce_backend.dto;

import java.util.List;

public record ProductRecommendationGroupDTO(
    String baseProduct,
    List<ProductRecommendationDTO> topRelatedProducts
) {}