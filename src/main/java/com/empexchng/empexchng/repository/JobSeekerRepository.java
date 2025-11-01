package com.empexchng.empexchng.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.empexchng.empexchng.model.JobSeeker;

public interface JobSeekerRepository extends JpaRepository<JobSeeker, String> {
  JobSeeker findBySeekerId(String seekerId);
  JobSeeker findByUser_Email(String email);
  List<JobSeeker> findByIsActiveTrue();
}

