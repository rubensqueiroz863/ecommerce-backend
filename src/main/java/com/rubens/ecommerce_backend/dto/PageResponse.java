package com.rubens.ecommerce_backend.dto;

import java.util.List;

public record PageResponse<T>(
    List<T> data,
    boolean hasMore
) {}
