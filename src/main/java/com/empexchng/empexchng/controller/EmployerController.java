package com.empexchng.empexchng.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.empexchng.empexchng.model.VacancyRequest;
import com.empexchng.empexchng.repository.UserRepository;

@Controller
@RequestMapping("/employer") // All URLs in this file start with /employer
public class EmployerController {

    @Autowired
    private UserRepository userRepository;

    // Your demo data
    private static final List<VacancyRequest> DEMO_REQUESTS = Arrays.asList(
            new VacancyRequest(1L, "John Doe", "john@example.com", "https://randomuser.me/api/portraits/men/32.jpg", "Software Engineer", "Experienced full-stack developer..."),
            new VacancyRequest(2L, "Mary Smith", "mary@example.com", "https://randomuser.me/api/portraits/women/65.jpg", "Marketing Manager", "Expert in digital marketing and SEO..."),
            new VacancyRequest(3L, "David Johnson", "david@example.com", "https://randomuser.me/api/portraits/men/45.jpg", "Data Analyst", "Skilled in SQL and Tableau..."),
            new VacancyRequest(4L, "Ella Brown", "ella@example.com", "https://randomuser.me/api/portraits/women/33.jpg", "Software Engineer", "Passionate UI/UX designer..."),
            new VacancyRequest(5L, "Michael Lee", "michael@example.com", "https://randomuser.me/api/portraits/men/51.jpg", "Marketing Manager", "AWS Certified DevOps professional.")
    );

    // Helper method to group data
    private Map<String, List<VacancyRequest>> getGroupedRequests() {
        return DEMO_REQUESTS.stream()
                .collect(Collectors.groupingBy(VacancyRequest::getVacancyTitle));
    }

    // Handles /employer/dashboard
    @GetMapping("/dashboard")
public String showDashboard(Model model,
    @CookieValue(value = "ee_email", required = false) String emailCookie) {

  model.addAttribute("requests", DEMO_REQUESTS);
  model.addAttribute("groupedRequests", getGroupedRequests());

  String displayName = null;
  if (emailCookie != null && !emailCookie.isBlank()) {
    displayName = userRepository.findByEmail(emailCookie).getName(); // only the name column
  }
  model.addAttribute("employerName", displayName != null ? displayName : "Guest");

  return "employer/dashboard";
}

    // Handles the "User Applications" link from the sidebar
    @GetMapping("/applications")
    public String showApplications(Model model) {
        model.addAttribute("requests", DEMO_REQUESTS);
        model.addAttribute("groupedRequests", getGroupedRequests());
        model.addAttribute("employerName", "Demo Employer");
        return "employer/dashboard"; // Also serves the dashboard HTML
    }

    // --- This is the controller for the code you just posted ---
    
    // 1. Handles the "Vacancies" link from the sidebar
    @GetMapping("/vacancies")
    public String showVacancyForm(Model model) {
        // Your vacancies.html file uses employerName in the header
        model.addAttribute("employerName", "Demo Employer");
        return "vacancies"; // Serves the vacancies.html file
    }
    
    // 2. Handles the form submission from vacancies.html
    @PostMapping("/vacancies")
    public String createVacancy(@RequestParam("title") String title, 
                                @RequestParam("description") String description,
                                @RequestParam("location") String location,
                                @RequestParam("skills_required") String skills) {
        
        // This is where you would save the data to the database
        System.out.println("New Vacancy Posted!");
        System.out.println("Title: " + title);
        System.out.println("Description: " + description);
        System.out.println("Location: " + location);
        System.out.println("Skills: " + skills);

        // After posting, send user back to the dashboard
        return "redirect:/employer/dashboard";
    }
    
    // TODO: Add @GetMapping("/profile") here
}