package com.rubens.ecommerce_backend.dto;

public record ClicksPerProductPerMonthDTO(
    String productId,
    String productName,
    Integer month,
    Long clicks
) {}