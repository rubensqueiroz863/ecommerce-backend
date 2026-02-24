package com.rubens.ecommerce_backend.model;

import java.util.UUID;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "subcategories")
public class SubCategory {

    @Id
    @Column
    private String id = UUID.randomUUID().toString();

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String slug;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
}
