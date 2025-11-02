package com.empexchng.empexchng.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.empexchng.empexchng.model.Vacancy;

public interface VacancyRepository extends JpaRepository<Vacancy, Long> {}