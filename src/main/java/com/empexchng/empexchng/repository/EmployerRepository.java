package com.empexchng.empexchng.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.empexchng.empexchng.model.Employer;

public interface EmployerRepository extends JpaRepository<Employer, String> {
  Employer findByEmployerId(String employerId);
  List<Employer> findByIsActiveTrue();
  Employer findByUser_Email(String email);                     // map from cookie email to employer

  @Modifying
  @Query("update Employer e set e.isActive = :active where e.employerId = :id")
  int setActive(@Param("id") String employerId, @Param("active") boolean active);
}

