package com.empexchng.empexchng.repository;

import com.empexchng.empexchng.model.Job;
import com.empexchng.empexchng.model.JobSeeker;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JobRepository extends JpaRepository<Job, Long>, JpaSpecificationExecutor<Job> {
  List<Job> findByEmployer_EmployerId(String employerId);
  List<Job> findByIsActiveTrue();
  List<Job> findByTitleContainingIgnoreCaseAndIsActiveTrue(String q);

  // Paginated listings for UI
  Page<Job> findByIsActiveTrue(Pageable pageable);             // use Pageable from service/controller [web:636][web:630]

  @Modifying
  @Query("update Job j set j.isActive = :active where j.jobId = :id")
  int setActive(@Param("id") Long jobId, @Param("active") boolean active);
}

