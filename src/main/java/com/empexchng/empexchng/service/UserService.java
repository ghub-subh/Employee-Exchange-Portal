package com.empexchng.empexchng.service;

import com.empexchng.empexchng.model.Employer;
import com.empexchng.empexchng.model.JobSeeker;
import com.empexchng.empexchng.model.User;
import com.empexchng.empexchng.repository.EmployerRepository;
import com.empexchng.empexchng.repository.JobSeekerRepository;
import com.empexchng.empexchng.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    // --- Repositories and Encoder ---
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmployerRepository employerRepository;

    @Autowired
    private JobSeekerRepository jobSeekerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // --- NEW: EmailService Dependency ---
    @Autowired
    private EmailService emailService;
    // ------------------------------------

    @Transactional
    public String registerUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            return "Email already registered!";
        }

        if (userRepository.existsByUserId(user.getUserId())) {
            return "User ID already registered!";
        }

        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        if (user.getUserId() == null || user.getUserId().isEmpty()) {
            user.setUserId(generateUserId(user.getRole()));
        }

        user.setApproved(false);
        
        User savedUser = userRepository.save(user);

        // Create associated profile (Employer/JobSeeker)
        if ("EMPLOYER".equals(user.getRole())) {
            Employer employer = Employer.builder()
                        .user(savedUser)
                        .companyName(savedUser.getName())
                        .isActive(true)
                        .build();
            employerRepository.save(employer);
        } else if ("JOB_SEEKER".equals(user.getRole())) {
            JobSeeker seeker = JobSeeker.builder()
                        .user(savedUser)
                        .isActive(true)
                        .build();
            jobSeekerRepository.save(seeker);
        }

        // --- NEW: Send Registration Confirmation Email ---
        String subject = "Registration Successful - Awaiting Approval";
        String body = "<p>Dear " + savedUser.getName() + ",</p>"
                    + "<p>Thank you for registering with EmpExchang! Your user ID is: <b>" + savedUser.getUserId() + "</b>.</p>"
                    + "<p>Your account has been successfully created and is now **awaiting admin approval**. You will receive another email once your account is activated.</p>"
                    + "<p>Thank you, The EmpExchang Team.</p>";
        
        emailService.sendEmail(savedUser.getEmail(), subject, body);
        // --------------------------------------------------

        return "User registered successfully! Please wait for admin approval. A confirmation email has been sent.";
    }
    
    @Transactional
    public void approveUser(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID: " + userId));
        
        // Only proceed if the user hasn't been approved yet
        if (!user.isApproved()) {
            user.setApproved(true);
            userRepository.save(user);

            // Activate associated profile
            if ("EMPLOYER".equals(user.getRole())) {
                Employer employer = employerRepository.findById(userId).orElse(null);
                if (employer != null) {
                    employer.setIsActive(true);
                    employerRepository.save(employer);
                }
            } else if ("JOB_SEEKER".equals(user.getRole())) {
                JobSeeker seeker = jobSeekerRepository.findById(userId).orElse(null);
                if (seeker != null) {
                    seeker.setIsActive(true);
                    jobSeekerRepository.save(seeker);
                }
            }

            // --- NEW: Send Account Approval Email ---
            String subject = "Account Activated! Welcome to EmpExchang";
            String body = "<p>Dear " + user.getName() + ",</p>"
                        + "<p>Great news! Your EmpExchang account (User ID: <b>" + user.getUserId() + "</b>) has been **approved and activated** by the admin.</p>"
                        + "<p>You can now log in and begin using the platform!</p>"
                        + "<p>Best regards, The EmpExchang Team.</p>";
            
            emailService.sendEmail(user.getEmail(), subject, body);
            // ------------------------------------------
        }
    }

    private String generateUserId(String role) {
        long count = userRepository.countByRole(role);
        // Using substring to get the first character (E or J)
        return role.substring(0, 1) + String.format("%04d", count + 1);
    }
}