package com.empexchng.empexchng.model;

import java.time.Instant;
import java.util.*;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
    name = "applications",
    uniqueConstraints = @UniqueConstraint(name = "uq_app_job_seeker", columnNames = {"job_id","seeker_id"})
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "application_id")
    private Long applicationId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;

    @ManyToOne(optional = false)
    @JoinColumn(name = "seeker_id", nullable = false)
    private JobSeeker seeker;

    @Column(nullable = false, length = 20)
    private String status;

    // --- ADD @Builder.Default HERE ---
    @Builder.Default
    @Column(name = "applied_at", nullable = false, updatable = false)
    private Instant appliedAt = Instant.now();

    @Column(name = "decided_at")
    private Instant decidedAt;

    @Lob
    private String notes;
}