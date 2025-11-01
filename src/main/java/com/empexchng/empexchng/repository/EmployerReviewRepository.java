package com.empexchng.empexchng.repository;

import com.empexchng.empexchng.model.EmployerReview;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
public interface EmployerReviewRepository extends JpaRepository<EmployerReview, Long> {

  // Average rating for public reviews of an employer
  @Query("select coalesce(avg(r.rating), 0) from EmployerReview r " +
         "where r.employer.employerId = :employerId and r.isPublic = true")
  double getPublicAverage(@Param("employerId") String employerId);

  // If you want average over all reviews (public and private)
  @Query("select coalesce(avg(r.rating), 0) from EmployerReview r " +
         "where r.employer.employerId = :employerId")
  double getAverageAll(@Param("employerId") String employerId);

  // Keep your other finders
  List<EmployerReview> findByEmployer_EmployerIdOrderByCreatedAtDesc(String employerId);
  List<EmployerReview> findBySeeker_SeekerIdOrderByCreatedAtDesc(String seekerId);
}
