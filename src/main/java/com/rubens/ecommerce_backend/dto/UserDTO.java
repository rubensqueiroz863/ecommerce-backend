package com.rubens.ecommerce_backend.dto;

public record UserDTO(
    String id,
    String email,
    String password,
    String name
) {}