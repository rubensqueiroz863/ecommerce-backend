package com.rubens.ecommerce_backend.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.rubens.ecommerce_backend.model.EventActivityLog;

public interface EventActivityLogRepository extends JpaRepository<EventActivityLog, String> {
    List<EventActivityLog> findAllByOrderByTimestampDesc();
}