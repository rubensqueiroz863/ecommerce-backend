package com.rubens.ecommerce_backend.dto;

public record ProductDTO (
    String id,
    String name,
    Double price,
    String photo,
    String subCategory
) {}
