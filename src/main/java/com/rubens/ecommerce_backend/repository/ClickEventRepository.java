package com.rubens.ecommerce_backend.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.rubens.ecommerce_backend.dto.ClicksPerProductPerMonthDTO;
import com.rubens.ecommerce_backend.dto.ProductRecommendationDTO;
import com.rubens.ecommerce_backend.model.ClickEvent;
import java.time.LocalDateTime;


public interface ClickEventRepository extends JpaRepository<ClickEvent, String> {

    @Query("""
        SELECT c.product, COUNT(c)
        FROM ClickEvent c
        WHERE c.user.id = :userId
        GROUP BY c.product
        ORDER BY COUNT(c) DESC
    """)
    List<Object[]> findMostClickedProductsByUser(String userId, Pageable pageable);

    @Query("""
        SELECT MONTH(c.createdAt), COUNT(c)
        FROM ClickEvent c
        WHERE c.createdAt BETWEEN :start AND :end
        GROUP BY MONTH(c.createdAt)
        ORDER BY MONTH(c.createdAt)
    """)
    List<Object[]> countClicksPerMonth(
        @Param("start") LocalDateTime start,
        @Param("end") LocalDateTime end
    );

    @Query("""
        SELECT new com.rubens.ecommerce_backend.dto.ClicksPerProductPerMonthDTO(
            c.product.id,
            c.product.name,
            MONTH(c.createdAt),
            COUNT(c)
        )
        FROM ClickEvent c
        WHERE c.createdAt BETWEEN :start AND :end
        GROUP BY c.product.id, c.product.name, MONTH(c.createdAt)
        ORDER BY c.product.id, MONTH(c.createdAt)
    """)
    List<ClicksPerProductPerMonthDTO> countClicksPerProductPerMonth(
        @Param("start") LocalDateTime start,
        @Param("end") LocalDateTime end
    );

    @Query("""
        SELECT new com.rubens.ecommerce_backend.dto.ProductRecommendationDTO(
            c2.product.id,
            c2.product.name,
            c2.product.price,
            COUNT(DISTINCT c1.user.id)
        )
        FROM ClickEvent c1
        JOIN ClickEvent c2
            ON c1.user = c2.user
        WHERE c1.product.id = :baseProductId
        AND c2.product.id <> :baseProductId
        GROUP BY c2.product.id, c2.product.name, c2.product.price
        ORDER BY COUNT(DISTINCT c1.user.id) DESC
    """)
    List<ProductRecommendationDTO> findTopRelatedProducts(
        @Param("baseProductId") String baseProductId
    );

    @Query("""
        SELECT new com.rubens.ecommerce_backend.dto.ProductRecommendationDTO(
            c2.product.id,
            c2.product.name,
            c2.product.price,
            COUNT(DISTINCT c2.user.id)
        )
        FROM ClickEvent c1
        JOIN ClickEvent cOtherUser
            ON c1.product = cOtherUser.product
        JOIN ClickEvent c2
            ON cOtherUser.user = c2.user
        WHERE c1.user.id = :userId
            AND cOtherUser.user.id <> :userId
            AND c2.product.id NOT IN (
                SELECT c3.product.id
                FROM ClickEvent c3
                WHERE c3.user.id = :userId
            )
        GROUP BY c2.product.id, c2.product.name, c2.product.price
        ORDER BY COUNT(DISTINCT c2.user.id) DESC
    """)
    List<ProductRecommendationDTO> findRecommendedProductsForUser(
        @Param("userId") String userId
    );

    void deleteByProductId(String id);

    void deleteByUserId(String id);
}