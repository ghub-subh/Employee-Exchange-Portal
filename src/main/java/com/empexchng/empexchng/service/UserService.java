package com.empexchng.empexchng.service;

import com.empexchng.empexchng.model.Employer;
import com.empexchng.empexchng.model.JobSeeker;
import com.empexchng.empexchng.repository.EmployerRepository;
import com.empexchng.empexchng.repository.JobSeekerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.empexchng.empexchng.model.User;
import com.empexchng.empexchng.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmployerRepository employerRepository;

    @Autowired
    private JobSeekerRepository jobSeekerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

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

        if ("EMPLOYER".equals(user.getRole())) {
            Employer employer = Employer.builder()
                    .user(savedUser) // Only set the user
                    .companyName(savedUser.getName())
                    .isActive(true)
                    .build();
            employerRepository.save(employer);
        } else if ("JOB_SEEKER".equals(user.getRole())) {
            JobSeeker seeker = JobSeeker.builder()
                    .user(savedUser) // Only set the user
                    .isActive(true)
                    .build();
            jobSeekerRepository.save(seeker);
        }

        return "User registered successfully! Please wait for admin approval.";
    }
    
    @Transactional
    public void approveUser(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID: " + userId));
        user.setApproved(true);
        userRepository.save(user);

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
    }

    private String generateUserId(String role) {
        long count = userRepository.countByRole(role);
        return role.charAt(0) + String.format("%04d", count + 1);
    }
}