package com.rubens.ecommerce_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rubens.ecommerce_backend.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByNameContainingIgnoreCase(String name);
    
    List<Product> findBySubCategory_Slug(String slug);

    List<Product> findBySubCategory_Category_Slug(String slug);
}
