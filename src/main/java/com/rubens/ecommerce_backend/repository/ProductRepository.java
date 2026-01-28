package com.rubens.ecommerce_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rubens.ecommerce_backend.model.Product;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);

    Page<Product> findBySubCategory_Slug(String slug, Pageable pageable);

    Page<Product> findBySubCategory_Category_Slug(String slug, Pageable pageable);

    Optional<Product> findById(int id);
}
