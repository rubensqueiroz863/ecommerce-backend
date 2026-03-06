package com.rubens.ecommerce_backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rubens.ecommerce_backend.model.SearchHistory;

public interface SearchHistoryRepository extends JpaRepository<SearchHistory, String> {

    List<SearchHistory> findTop5ByUserIdOrderByCreatedAtDesc(String userId);
    Optional<SearchHistory> findByUserIdAndQuery(String userId, String query);
}
