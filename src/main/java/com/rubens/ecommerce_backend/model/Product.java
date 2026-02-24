package com.rubens.ecommerce_backend.model;

import java.util.UUID;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "products")
public class Product {

    @Id
    @Column
    private String id = UUID.randomUUID().toString();

    @Column(nullable = false)
    private String name;

    private Double price;
    private String photo;

    @ManyToOne
    @JoinColumn(name = "subcategory_id", nullable = false)
    private SubCategory subCategory;
}
