package com.rubens.ecommerce_backend.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.rubens.ecommerce_backend.model.UserActivityLog;

public interface UserActivityLogRepository extends JpaRepository<UserActivityLog, String> {
    List<UserActivityLog> findAllByOrderByTimestampDesc();
}