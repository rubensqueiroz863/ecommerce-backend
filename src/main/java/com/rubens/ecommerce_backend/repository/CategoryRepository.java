package com.rubens.ecommerce_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.rubens.ecommerce_backend.model.Category;

public interface CategoryRepository extends JpaRepository<Category, String> {
}
