package com.rubens.ecommerce_backend.model;

import java.util.UUID;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @Column
    private String id = UUID.randomUUID().toString();

    @Column(unique = true, nullable = false)
    private String email;

    private String password;

    private String name;

    @Enumerated(EnumType.STRING)
    private Role role = Role.ROLE_USER;
}
