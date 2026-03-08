package com.rubens.ecommerce_backend.dto;

public record ProductRequestDTO(
    String name,
    Double price,
    String photo,
    String subCategory
) {}