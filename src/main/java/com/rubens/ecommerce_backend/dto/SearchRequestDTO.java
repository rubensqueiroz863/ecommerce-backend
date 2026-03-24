package com.rubens.ecommerce_backend.dto;

import lombok.Data;

@Data
public class SearchRequestDTO {
    private String query;
    private String userEmail;

}