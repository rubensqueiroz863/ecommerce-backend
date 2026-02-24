package com.rubens.ecommerce_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.rubens.ecommerce_backend.model.SubCategory;

public interface SubCategoryRepository extends JpaRepository<SubCategory, String> {
}
