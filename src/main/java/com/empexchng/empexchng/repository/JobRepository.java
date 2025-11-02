package com.empexchng.empexchng.repository;

import com.empexchng.empexchng.model.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JobRepository extends JpaRepository<Job, Long>, JpaSpecificationExecutor<Job> {

    List<Job> findByIsApproved(boolean isApproved);

    @Query("SELECT j FROM Job j JOIN FETCH j.employer WHERE j.isApproved = :isApproved")
    List<Job> findByIsApprovedWithEmployer(@Param("isApproved") boolean isApproved);

    // --- ADD THIS METHOD ---
    List<Job> findByIsApprovedTrueAndIsActiveTrueOrderByCreatedAtDesc();

    List<Job> findByEmployer_EmployerId(String employerId);
    List<Job> findByIsActiveTrue();
    List<Job> findByTitleContainingIgnoreCaseAndIsActiveTrue(String q);

    @Modifying
    @Query("update Job j set j.isActive = :active where j.jobId = :id")
    int setActive(@Param("id") Long jobId, @Param("active") boolean active);
}