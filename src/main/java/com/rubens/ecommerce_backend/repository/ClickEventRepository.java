package com.rubens.ecommerce_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rubens.ecommerce_backend.model.ClickEvent;

public interface ClickEventRepository extends JpaRepository<ClickEvent, String> {
}