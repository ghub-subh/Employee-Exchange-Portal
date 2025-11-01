package com.empexchng.empexchng.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.empexchng.empexchng.model.CompanyInfo;
public interface CompanyInfoRepository extends JpaRepository<CompanyInfo, String> {
  CompanyInfo findByEmployer_EmployerId(String employerId);
}

