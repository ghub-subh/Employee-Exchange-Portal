package com.empexchng.empexchng.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.empexchng.empexchng.model.Application;
import com.empexchng.empexchng.model.Employer; // Import this
import com.empexchng.empexchng.model.User;
import com.empexchng.empexchng.repository.ApplicationRepository;
import com.empexchng.empexchng.repository.EmployerRepository; // Import this
import com.empexchng.empexchng.service.ApplicationService;
import com.empexchng.empexchng.service.EmployerService; // Import this
import com.empexchng.empexchng.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.empexchng.empexchng.repository.UserRepository;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/employer")
public class EmployerController {
    
    // ... (UserRepository, JobService, ApplicationRepository are unchanged) ...
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JobService jobService;
    @Autowired
    private ApplicationRepository applicationRepository;
    @Autowired
    private ApplicationService applicationService;

    // --- ADD THESE ---
    @Autowired
    private EmployerRepository employerRepository;
    @Autowired
    private EmployerService employerService;


    // ... (dashboard, applications, vacancies, approve/reject methods are all unchanged) ...
    
    @GetMapping("/dashboard")
    public String employerDashboard(Model model,
                                    @AuthenticationPrincipal User user) {
        
        List<Application> applications = applicationRepository.findByEmployerId(user.getUserId());
        Map<String, List<Application>> groupedApplications = applications.stream()
                .collect(Collectors.groupingBy(app -> app.getJob().getTitle()));

        model.addAttribute("employerName", user.getName());
        model.addAttribute("applications", applications);
        model.addAttribute("groupedApplications", groupedApplications);
        return "employer/dashboard";
    }

    @GetMapping("/applications")
    public String showApplications(Model model,
                                   @AuthenticationPrincipal User user) {
        
        List<Application> applications = applicationRepository.findByEmployerId(user.getUserId());
        Map<String, List<Application>> groupedApplications = applications.stream()
                .collect(Collectors.groupingBy(app -> app.getJob().getTitle()));

        model.addAttribute("employerName", user.getName());
        model.addAttribute("applications", applications);
        model.addAttribute("groupedApplications", groupedApplications);
        return "employer/dashboard";
    }

    @GetMapping("/vacancies")
    public String showVacancyForm(Model model,
                                  @AuthenticationPrincipal User user) {
        String name = (user != null) ? user.getName() : "Guest";
        model.addAttribute("employerName", name);
        return "employer/vacancies";
    }

    @PostMapping("/vacancies")
    public String createVacancy(@RequestParam("title") String title,
                                @RequestParam("description") String description,
                                @RequestParam("location") String location,
                                @RequestParam("skills_required") String skills,
                                @AuthenticationPrincipal User user,
                                RedirectAttributes ra) {
        try {
            jobService.createJob(title, description, location, skills, user);
            ra.addFlashAttribute("successMessage", "Vacancy posted! It is pending admin approval.");
        } catch (Exception e) {
            e.printStackTrace();
            ra.addFlashAttribute("errorMessage", "Error posting vacancy: " + e.getMessage());
        }

        return "redirect:/employer/dashboard";
    }

    @PostMapping("/application/approve/{id}")
    public String approveApplication(@PathVariable("id") Long applicationId, RedirectAttributes ra) {
        try {
            applicationService.updateApplicationStatus(applicationId, "ACCEPTED");
            ra.addFlashAttribute("successMessage", "Application approved.");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMessage", "Error: " + e.getMessage());
        }
        return "redirect:/employer/dashboard";
    }

    @PostMapping("/application/reject/{id}")
    public String rejectApplication(@PathVariable("id") Long applicationId, RedirectAttributes ra) {
        try {
            applicationService.updateApplicationStatus(applicationId, "REJECTED");
            ra.addFlashAttribute("successMessage", "Application rejected.");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMessage", "Error: " + e.getMessage());
        }
        return "redirect:/employer/dashboard";
    }

    // --- ADD THESE 2 NEW METHODS ---
    
    @GetMapping("/profile")
    public String showProfilePage(@AuthenticationPrincipal User user, Model model) {
        Employer employer = employerRepository.findById(user.getUserId())
                .orElse(new Employer()); // Get the employer profile
        
        model.addAttribute("user", user);
        model.addAttribute("employer", employer); // Add profile details to the model
        return "employer/profile";
    }

    @PostMapping("/profile")
    public String updateProfile(
            @AuthenticationPrincipal User user,
            @RequestParam String name,
            @RequestParam String companyName,
            @RequestParam String location,
            @RequestParam String website,
            @RequestParam String description,
            RedirectAttributes ra) {
        
        try {
            employerService.updateProfile(user, name, companyName, location, website, description);
            ra.addFlashAttribute("successMessage", "Profile updated successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            ra.addFlashAttribute("errorMessage", "Error updating profile: " + e.getMessage());
        }
        
        return "redirect:/employer/profile";
    }
}