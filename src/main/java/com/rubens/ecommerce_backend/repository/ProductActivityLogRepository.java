package com.rubens.ecommerce_backend.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.rubens.ecommerce_backend.model.ProductActivityLog;

public interface ProductActivityLogRepository extends JpaRepository<ProductActivityLog, String> {
    List<ProductActivityLog> findAllByOrderByTimestampDesc();
}