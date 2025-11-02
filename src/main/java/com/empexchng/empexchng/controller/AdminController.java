package com.empexchng.empexchng.controller;

import com.empexchng.empexchng.model.Job; // Import Job
import com.empexchng.empexchng.model.User;
import com.empexchng.empexchng.repository.JobRepository; // Import JobRepository
import com.empexchng.empexchng.repository.UserRepository;
import com.empexchng.empexchng.service.JobService; // Import JobService
import com.empexchng.empexchng.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;
    
    // --- ADD THESE ---
    @Autowired
    private JobRepository jobRepository;
    
    @Autowired
    private JobService jobService;

    // --- THIS METHOD IS UPDATED ---
    @GetMapping("/panel")
    public String showAdminPanel(Model model, @AuthenticationPrincipal User admin) {

        List<User> unapprovedUsers = userRepository.findByIsApproved(false);
        List<Job> unapprovedJobs = jobRepository.findByIsApproved(false); // Get unapproved jobs

        model.addAttribute("adminName", admin.getName());
        model.addAttribute("unapprovedUsers", unapprovedUsers);
        model.addAttribute("unapprovedJobs", unapprovedJobs); // Add jobs to model

        return "admin/panel";
    }

    @PostMapping("/approve/user/{userId}")
    public String approveUser(@PathVariable("userId") String userId, RedirectAttributes ra) {
        try {
            userService.approveUser(userId);
            ra.addFlashAttribute("successMessage", "User " + userId + " approved successfully.");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMessage", "Error approving user: " + e.getMessage());
        }
        return "redirect:/admin/panel";
    }
    
    // --- ADD THIS NEW METHOD ---
    @PostMapping("/approve/job/{jobId}")
    public String approveJob(@PathVariable("jobId") Long jobId, RedirectAttributes ra) {
        try {
            jobService.approveJob(jobId);
            ra.addFlashAttribute("successMessage", "Job #" + jobId + " approved successfully.");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMessage", "Error approving job: " + e.getMessage());
        }
        return "redirect:/admin/panel";
    }
}