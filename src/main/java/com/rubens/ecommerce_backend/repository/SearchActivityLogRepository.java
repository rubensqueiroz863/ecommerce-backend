package com.rubens.ecommerce_backend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.rubens.ecommerce_backend.model.SearchActivityLog;

public interface SearchActivityLogRepository extends JpaRepository<SearchActivityLog, String> {
    Page<SearchActivityLog> findAllByOrderByTimestampDesc(Pageable page);
}