package com.rubens.ecommerce_backend.model;

import java.time.LocalDateTime;
import java.util.UUID;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class ClickEvent {
    @Id
    @Column
    private String id = UUID.randomUUID().toString();

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne
    private User user;

    @ManyToOne
    private Product product;

    public ClickEvent(User user, Product product) {
        this.user = user;
        this.product = product;
    }
}
