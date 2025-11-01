package com.empexchng.empexchng.model;

import java.time.Instant;
import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Table(name = "vacancies")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vacancy {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "vacancy_id")
  private Long vacancyId;

  @ManyToOne(optional = false)
  @JoinColumn(name = "job_id", nullable = false)
  private Job job;

  @Column(nullable = false, length = 20)
  private String status;        // OPEN / FILLED / CLOSED

  private LocalDate startDate;

  @Lob
  private String notes;

  @Column(name = "created_at", nullable = false, updatable = false)
  private Instant createdAt = Instant.now();

  @Column(name = "filled_at")
  private Instant filledAt;
}

