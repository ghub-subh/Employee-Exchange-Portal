package com.empexchng.empexchng.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
@Entity
@Table(name = "jobs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Job {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "job_id")
  private Long jobId;

  @ManyToOne(optional = false)
  @JoinColumn(name = "employer_id", nullable = false)
  private Employer employer;

  @Column(nullable = false, length = 200)
  private String title;

  @Lob
  @Column(nullable = false)
  private String description;

  @Column(length = 150)
  private String location;

  @Column(name = "employment_type", length = 50)
  private String employmentType;

  @Column(name = "salary_min", precision = 12, scale = 2)
  private BigDecimal salaryMin;

  @Column(name = "salary_max", precision = 12, scale = 2)
  private BigDecimal salaryMax;

  @Column(name = "is_active", nullable = false)
  private Boolean isActive = true;

  @Column(name = "created_at", nullable = false, updatable = false)
  private Instant createdAt = Instant.now();

  @OneToMany(mappedBy = "job", cascade = CascadeType.ALL, orphanRemoval = false)
  private List<Application> applications = new ArrayList<>();
}

