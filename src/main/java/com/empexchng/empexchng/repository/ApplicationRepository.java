package com.empexchng.empexchng.repository;

import com.empexchng.empexchng.model.Application;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
  // Job seeker views their applications
  List<Application> findBySeeker_SeekerIdOrderByAppliedAtDesc(String seekerId);

  // Employer views applications to a job
  List<Application> findByJob_JobIdOrderByAppliedAtDesc(Long jobId);

  // Prevent duplicate apply (enforced also by unique constraint)
  Application findByJob_JobIdAndSeeker_SeekerId(Long jobId, String seekerId);

  long countByJob_JobIdAndStatus(Long jobId, String status);

  @Modifying
  @Query("update Application a set a.status = :status, a.decidedAt = CURRENT_TIMESTAMP where a.applicationId = :id")
  int setStatus(@Param("id") Long applicationId, @Param("status") String status); // PENDING/ACCEPTED/REJECTED/WITHDRAWN

  @Modifying
  @Query("delete from Application a where a.job.jobId = :jobId and a.status = 'PENDING'")
  int deletePendingByJob(@Param("jobId") Long jobId);
}

