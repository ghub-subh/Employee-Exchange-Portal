package com.empexchng.empexchng.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "job_seekers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobSeeker {
    @Id
    @Column(name = "seeker_id", length = 200)
    private String seekerId;

    @MapsId
    @OneToOne(optional = false)
    @JoinColumn(name = "seeker_id")
    private User user;

    @Column(name = "resume_url", length = 500)
    private String resumeUrl;

    @Lob
    @Column(name = "skills_text")
    private String skillsText;

    @Column(name = "experience_years", precision = 4, scale = 1)
    private BigDecimal experienceYears;

    @Column(length = 150)
    private String location;

    // --- ADD @Builder.Default HERE ---
    @Builder.Default
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    // --- ADD @Builder.Default HERE ---
    @Builder.Default
    @OneToMany(mappedBy = "seeker", cascade = CascadeType.ALL, orphanRemoval = false)
    private List<Application> applications = new ArrayList<>();

    // --- ADD @Builder.Default HERE ---
    @Builder.Default
    @OneToMany(mappedBy = "seeker", cascade = CascadeType.ALL, orphanRemoval = false)
    private List<EmployerReview> reviews = new ArrayList<>();
}