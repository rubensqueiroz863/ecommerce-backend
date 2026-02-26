package com.rubens.ecommerce_backend.dto;

import com.rubens.ecommerce_backend.model.Product;

public record MostClickedProductDTO(
    Product product,
    Long clicks
) {}