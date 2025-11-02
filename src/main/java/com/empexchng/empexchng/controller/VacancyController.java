package com.empexchng.empexchng.controller;

import com.empexchng.empexchng.DTO.VacancyForm;
import com.empexchng.empexchng.model.Vacancy;
import com.empexchng.empexchng.service.VacancyService;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/employer/vacancy")
public class VacancyController {

    @Autowired
  private  VacancyService vacancyService;

    @GetMapping("/new")
  public String newVacancy(Model model) {
    model.addAttribute("vacancyForm", new VacancyForm());
    model.addAttribute("jobs", vacancyService.listJobs());
    model.addAttribute("statuses", List.of("OPEN","FILLED","CLOSED"));
    return "employer/vacancies";
  }

  @PostMapping({"", "/"})
  public String create(@ModelAttribute("vacancyForm") VacancyForm form,
                       RedirectAttributes ra) {
    vacancyService.createVacancy(form.getJobId(), form.getStatus(),
                                 form.getStartDate(), form.getNotes());
    ra.addFlashAttribute("message", "Vacancy submitted for approval");
    return "redirect:/employer/vacancy"; // or redirect:/employer/vacancies if that route lists
  }

  @GetMapping
  public String list(Model model) {
    model.addAttribute("vacancies", vacancyService.listVacancies());
    return "employer/vacancy-list";
  }
}

