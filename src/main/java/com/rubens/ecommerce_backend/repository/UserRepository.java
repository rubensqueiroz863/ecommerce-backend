package com.rubens.ecommerce_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.rubens.ecommerce_backend.model.User;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);
}
