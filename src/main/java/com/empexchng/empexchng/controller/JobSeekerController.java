package com.empexchng.empexchng.controller;

import com.empexchng.empexchng.model.Application;
import com.empexchng.empexchng.model.Job;
import com.empexchng.empexchng.model.JobSeeker;
import com.empexchng.empexchng.model.User;
import com.empexchng.empexchng.repository.ApplicationRepository;
import com.empexchng.empexchng.repository.JobRepository;
import com.empexchng.empexchng.repository.JobSeekerRepository;
import com.empexchng.empexchng.service.ApplicationService;
import com.empexchng.empexchng.service.JobSeekerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/jobseeker")
public class JobSeekerController {

    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private ApplicationService applicationService;
    @Autowired
    private JobSeekerRepository jobSeekerRepository;
    @Autowired
    private JobSeekerService jobSeekerService;
    @Autowired
    private ApplicationRepository applicationRepository;


    @GetMapping("/panel")
    public String showJobSeekerPanel(Model model, @AuthenticationPrincipal User user) {
        
        List<Job> availableJobs = jobRepository.findByIsApprovedTrueAndIsActiveTrueOrderByCreatedAtDesc();
        
        List<Application> myApplications = applicationRepository.findBySeeker_SeekerIdOrderByAppliedAtDesc(user.getUserId());
        Set<Long> appliedJobIds = myApplications.stream()
                                                .map(app -> app.getJob().getJobId())
                                                .collect(Collectors.toSet());
        
        model.addAttribute("jobSeekerName", user.getName());
        model.addAttribute("availableJobs", availableJobs);
        model.addAttribute("appliedJobIds", appliedJobIds);
        
        return "jobseeker/panel";
    }
    
    @GetMapping("/my-applications")
    public String showMyApplications(Model model, @AuthenticationPrincipal User user) {
        
        List<Application> myApplications = applicationRepository.findBySeeker_SeekerIdOrderByAppliedAtDesc(user.getUserId());
        
        model.addAttribute("jobSeekerName", user.getName());
        model.addAttribute("myApplications", myApplications);
        
        return "jobseeker/my-applications";
    }

    @PostMapping("/apply/{jobId}")
    public String applyForJob(@PathVariable("jobId") Long jobId,
                              @AuthenticationPrincipal User user,
                              RedirectAttributes ra) {
        try {
            applicationService.applyForJob(jobId, user);
            ra.addFlashAttribute("successMessage", "Successfully applied for job!");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMessage", "Error applying for job: " + e.getMessage());
        }
        
        return "redirect:/jobseeker/panel";
    }

    @GetMapping("/profile")
    public String showProfilePage(@AuthenticationPrincipal User user, Model model) {
        JobSeeker seeker = jobSeekerRepository.findById(user.getUserId())
                .orElse(new JobSeeker());
        
        model.addAttribute("user", user);
        model.addAttribute("seeker", seeker);
        return "jobseeker/profile";
    }

    @PostMapping("/profile")
    public String updateProfile(
            @AuthenticationPrincipal User user,
            @RequestParam String name,
            @RequestParam String location,
            @RequestParam String skillsText,
            @RequestParam(required = false) BigDecimal experienceYears,
            RedirectAttributes ra) {
        
        try {
            jobSeekerService.updateProfile(user, name, location, skillsText, experienceYears);
            ra.addFlashAttribute("successMessage", "Profile updated successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            ra.addFlashAttribute("errorMessage", "Error updating profile: " + e.getMessage());
        }
        
        return "redirect:/jobseeker/profile";
    }
}