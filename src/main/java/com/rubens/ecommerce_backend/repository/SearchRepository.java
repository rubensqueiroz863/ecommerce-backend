package com.rubens.ecommerce_backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rubens.ecommerce_backend.model.Search;

public interface SearchRepository extends JpaRepository<Search, String> {

    List<Search> findTop5ByUserIdOrderByCreatedAtDesc(String userId);
    Optional<Search> findByUserIdAndQuery(String userId, String query);
}
