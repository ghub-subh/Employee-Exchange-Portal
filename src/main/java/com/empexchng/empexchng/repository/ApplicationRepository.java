package com.empexchng.empexchng.repository;

import com.empexchng.empexchng.model.Application;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ApplicationRepository extends JpaRepository<Application, Long> {

    // --- ADD THIS NEW QUERY ---
    @Query("SELECT a FROM Application a WHERE a.job.employer.employerId = :employerId ORDER BY a.appliedAt DESC")
    List<Application> findByEmployerId(@Param("employerId") String employerId);

    boolean existsByJob_JobIdAndSeeker_SeekerId(Long jobId, String seekerId);
    List<Application> findBySeeker_SeekerIdOrderByAppliedAtDesc(String seekerId);
    List<Application> findByJob_JobIdOrderByAppliedAtDesc(Long jobId);
    Application findByJob_JobIdAndSeeker_SeekerId(Long jobId, String seekerId);
    long countByJob_JobIdAndStatus(Long jobId, String status);

    @Modifying
    @Query("update Application a set a.status = :status, a.decidedAt = CURRENT_TIMESTAMP where a.applicationId = :id")
    int setStatus(@Param("id") Long applicationId, @Param("status") String status);

    @Modifying
    @Query("delete from Application a where a.job.jobId = :jobId and a.status = 'PENDING'")
    int deletePendingByJob(@Param("jobId") Long jobId);
}