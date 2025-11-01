package com.empexchng.empexchng.model;


import java.util.*;
import lombok.*;
import java.time.Instant;
import jakarta.persistence.*;

@Entity
@Table(name = "employer_reviews")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class EmployerReview {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "review_id")
  private Long reviewId;

  @ManyToOne(optional = false)
  @JoinColumn(name = "employer_id", nullable = false)
  private Employer employer;

  @ManyToOne(optional = true)
  @JoinColumn(name = "seeker_id")
  private JobSeeker seeker; // nullable due to ON DELETE SET NULL

  @Column(nullable = false)
  private Integer rating; // 1..5, validate in service

  @Column(length = 200)
  private String title;

  @Lob
  @Column(name = "review_text")
  private String reviewText;

  @Column(name = "created_at", nullable = false, updatable = false)
  private Instant createdAt = Instant.now();

  @Column(name = "is_public", nullable = false)
  private Boolean isPublic = true;
}

