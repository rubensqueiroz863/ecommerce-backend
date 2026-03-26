package com.rubens.ecommerce_backend.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "search_activity_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchActivityLog {

    @Id
    @Column
    @Builder.Default
    private String id = UUID.randomUUID().toString();

    private String searchId;
    private String performedBy;
    private String action;
    private String details;
    private LocalDateTime timestamp;
}