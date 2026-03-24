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

    private String userId;
    private String performedBy;
    private String action;
    private String details;
    private LocalDateTime timestamp;
}