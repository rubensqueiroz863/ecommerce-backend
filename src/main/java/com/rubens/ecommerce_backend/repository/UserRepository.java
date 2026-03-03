package com.rubens.ecommerce_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.rubens.ecommerce_backend.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);
    List<User> findTop1000By();
}
