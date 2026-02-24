package com.rubens.ecommerce_backend.dto;

import com.rubens.ecommerce_backend.model.Product;
import com.rubens.ecommerce_backend.model.User;

public record ClickEventDTO(
    String id,
    User user,
    Product product
) {}