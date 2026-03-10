package com.rubens.ecommerce_backend.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "user_activity_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserActivityLog {

    @Id
    @Column
    @Builder.Default
    private String id = UUID.randomUUID().toString();

    private String userId;        // Usuário que sofreu a ação
    private String performedBy;   // Usuário que realizou a ação (admin)
    private String action;        // CREATE, UPDATE, DELETE
    private String details;       // Descrição do que foi alterado
    private LocalDateTime timestamp;
}