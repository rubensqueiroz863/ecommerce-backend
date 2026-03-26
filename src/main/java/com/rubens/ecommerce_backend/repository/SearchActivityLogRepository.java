package com.rubens.ecommerce_backend.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.rubens.ecommerce_backend.model.SearchActivityLog;

public interface SearchActivityLogRepository extends JpaRepository<SearchActivityLog, String> {
    List<SearchActivityLog> findAllByOrderByTimestampDesc();
}