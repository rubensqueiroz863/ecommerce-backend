package com.rubens.ecommerce_backend.model;

import java.time.LocalDateTime;
import java.util.UUID;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(
    uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "product_id"})
)
@NoArgsConstructor
@AllArgsConstructor
public class ClickEvent {

    @Id
    @Column
    private String id = UUID.randomUUID().toString();

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    public ClickEvent(User user, Product product) {
        this.user = user;
        this.product = product;
    }
}