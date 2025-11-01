package com.empexchng.empexchng.repository;

import com.empexchng.empexchng.model.EmployerReview;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
public interface EmployerReviewRepository extends JpaRepository<EmployerReview, Long> {
  List<EmployerReview> findByEmployer_EmployerIdOrderByCreatedAtDesc(String employerId);
  List<EmployerReview> findBySeeker_SeekerIdOrderByCreatedAtDesc(String seekerId);
  double averageRatingByEmployer_EmployerId(String employerId); // custom below

  @Query("select coalesce(avg(r.rating),0) from EmployerReview r where r.employer.employerId = :employerId and r.isPublic = true")
  double getPublicAverage(@Param("employerId") String employerId);

  @Modifying
  @Query("update EmployerReview r set r.isPublic = :pub where r.reviewId = :id")
  int setVisibility(@Param("id") Long reviewId, @Param("pub") boolean isPublic);
}
