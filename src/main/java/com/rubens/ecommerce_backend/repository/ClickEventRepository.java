package com.rubens.ecommerce_backend.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.rubens.ecommerce_backend.model.ClickEvent;

public interface ClickEventRepository extends JpaRepository<ClickEvent, String> {

    @Query("""
        SELECT c.product, COUNT(c)
        FROM ClickEvent c
        WHERE c.user.id = :userId
        GROUP BY c.product
        ORDER BY COUNT(c) DESC
    """)
    List<Object[]> findMostClickedProductsByUser(String userId, Pageable pageable);
}