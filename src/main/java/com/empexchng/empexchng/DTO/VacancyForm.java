package com.empexchng.empexchng.DTO;

import java.time.LocalDate;

import lombok.Data;

@Data
public class VacancyForm {
  private Long jobId;               // selected Job
  private String status;            // OPEN/FILLED/CLOSED
  private LocalDate startDate;
  private String notes;
}

