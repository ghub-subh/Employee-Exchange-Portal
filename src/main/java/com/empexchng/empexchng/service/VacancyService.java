package com.empexchng.empexchng.service;

import java.time.LocalDate;

import com.empexchng.empexchng.model.Job;
import java.time.Instant;
import java.util.List;
import org.springframework.stereotype.Service;

import com.empexchng.empexchng.model.Vacancy;
import com.empexchng.empexchng.repository.JobRepository;
import com.empexchng.empexchng.repository.VacancyRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VacancyService {
  private final VacancyRepository vacancyRepo;
  private final JobRepository jobRepo;

  @Transactional
public Vacancy createVacancy(Long jobId, String status, LocalDate startDate, String notes) {
  Job job = jobRepo.findById(jobId).orElseThrow(() -> new IllegalArgumentException("Invalid job"));
  Vacancy v = Vacancy.builder()
      .job(job)
      .status(status)
      .startDate(startDate)
      .notes(notes)
      .build();
  if ("FILLED".equalsIgnoreCase(status)) v.setFilledAt(Instant.now());
  return vacancyRepo.save(v);
}


  @Transactional
  public List<Job> listJobs() {
    return jobRepo.findAll();
  }

  @Transactional
  public List<Vacancy> listVacancies() {
    return vacancyRepo.findAll();
  }
}

