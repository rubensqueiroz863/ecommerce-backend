package com.rubens.ecommerce_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rubens.ecommerce_backend.model.SearchHistory;

public interface SearchHistoryRepository extends JpaRepository<SearchHistory, String> {

    List<SearchHistory> findTop5ByUserIdOrderByCreatedAtDesc(String userId);

}
