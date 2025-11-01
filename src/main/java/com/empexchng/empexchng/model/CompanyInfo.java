package com.empexchng.empexchng.model;


import java.math.BigDecimal;
import jakarta.persistence.*;
import lombok.*;
@Entity
@Table(name = "company_info")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyInfo {
  @Id
  @Column(name = "employer_id", length = 200)
  private String employerId;

  @MapsId
  @OneToOne(optional = false)
  @JoinColumn(name = "employer_id")
  private Employer employer;

  @Column(name = "founded_year")
  private Integer foundedYear;

  @Column(name = "headcount")
  private Integer headcount;

  @Column(length = 100)
  private String industry;

  @Column(name = "revenue_usd", precision = 14, scale = 2)
  private BigDecimal revenueUsd;

  @Column(name = "hq_location", length = 150)
  private String hqLocation;

  @Column(length = 200)
  private String website;

  @Lob
  @Column(name = "about_text")
  private String aboutText;
}

