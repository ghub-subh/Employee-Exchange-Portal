package com.empexchng.empexchng.model;

import java.time.Instant;
import java.util.*;
import jakarta.persistence.*;
import lombok.*;
@Entity
@Table(
  name = "company_policies",
  uniqueConstraints = @UniqueConstraint(name = "uq_policy_version", columnNames = {"employer_id","version_no"})
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyPolicy {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "policy_id")
  private Long policyId;

  @ManyToOne(optional = false)
  @JoinColumn(name = "employer_id", nullable = false)
  private Employer employer;

  @Column(name = "version_no", nullable = false)
  private Integer versionNo;

  @Column(nullable = false, length = 200)
  private String title;

  @Lob
  @Column(name = "pdf_blob", nullable = false)
  private byte[] pdfBlob; // LONGBLOB in MySQL

  @Column(name = "generated_at", nullable = false, updatable = false)
  private Instant generatedAt = Instant.now();

  @ManyToOne(optional = true)
  @JoinColumn(name = "generated_by")
  private User generatedBy;

  @Lob
  @Column(name = "summary_text")
  private String summaryText;
}

