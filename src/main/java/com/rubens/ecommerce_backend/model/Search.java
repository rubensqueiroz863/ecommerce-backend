package com.rubens.ecommerce_backend.model;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "search_history")
public class Search {

  @Id
  @Column
  private String id = UUID.randomUUID().toString();

  @Column(nullable = false)
  private String query;

  @Column(nullable = false)
  private LocalDateTime createdAt = LocalDateTime.now();

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  public Search() {
  }

  public Search(User user, String query) {
      this.user = user;
      this.query = query;
  }
}