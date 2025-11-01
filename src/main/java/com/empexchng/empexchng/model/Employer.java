package com.empexchng.empexchng.model;

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
import lombok.*;
;

@Entity
@Table(name = "employers")
@Getter
@Setter
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
public class Employer {
  @Id
  @Column(name = "employer_id", length = 200)
  private String employerId;

  @MapsId
  @OneToOne(optional = false)
  @JoinColumn(name = "employer_id")
  private User user; // role should be EMPLOYER

  @Column(name = "company_name", nullable = false, length = 200)
  private String companyName;

  @Column(length = 200)
  private String website;

  @Column(length = 150)
  private String location;

  @Lob
  private String description;

  @Column(name = "is_active", nullable = false)
  private Boolean isActive = true;

  @OneToMany(mappedBy = "employer", cascade = CascadeType.ALL, orphanRemoval = false)
  private List<Job> jobs = new ArrayList<>();

  @OneToMany(mappedBy = "employer", cascade = CascadeType.ALL, orphanRemoval = false)
  private List<EmployerReview> reviews = new ArrayList<>();

  @OneToOne(mappedBy = "employer", cascade = CascadeType.ALL, orphanRemoval = true)
  private CompanyInfo companyInfo;

  @OneToMany(mappedBy = "employer", cascade = CascadeType.ALL, orphanRemoval = false)
  private List<CompanyPolicy> policies = new ArrayList<>();
}

