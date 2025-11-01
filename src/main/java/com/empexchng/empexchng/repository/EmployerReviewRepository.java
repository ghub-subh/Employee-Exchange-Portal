package com.empexchng.empexchng.repository;

import com.empexchng.empexchng.model.EmployerReview;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
public interface EmployerReviewRepository extends JpaRepository<EmployerReview, Long> {

  // Lists (valid derived methods)
  List<EmployerReview> findByEmployer_EmployerIdOrderByCreatedAtDesc(String employerId);
  List<EmployerReview> findBySeeker_SeekerIdOrderByCreatedAtDesc(String seekerId);

  // AVERAGE via explicit JPQL
  @Query("select coalesce(avg(r.rating),0) from EmployerReview r where r.employer.employerId = :employerId")
  double getAverageAll(@Param("employerId") String employerId);

  // Public-only average (you already had)
  @Query("select coalesce(avg(r.rating),0) from EmployerReview r where r.employer.employerId = :employerId and r.isPublic = true")
  double getPublicAverage(@Param("employerId") String employerId);

  // Update visibility
  @Modifying
  @Query("update EmployerReview r set r.isPublic = :pub where r.reviewId = :id")
  int setVisibility(@Param("id") Long reviewId, @Param("pub") boolean isPublic);
}

