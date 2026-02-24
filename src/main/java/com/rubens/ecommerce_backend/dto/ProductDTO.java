package com.rubens.ecommerce_backend.dto;

import lombok.Data;

@Data
public class ProductDTO {

    private String id;
    private String name;
    private Double price;
    private String photo;
    private String category;
    private String subCategory;
}
