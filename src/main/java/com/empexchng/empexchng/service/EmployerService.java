package com.empexchng.empexchng.service;

import com.empexchng.empexchng.model.Employer;
import com.empexchng.empexchng.model.User;
import com.empexchng.empexchng.repository.EmployerRepository;
import com.empexchng.empexchng.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EmployerService {

    @Autowired
    private EmployerRepository employerRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public void updateProfile(User user, String name, String companyName, String location, String website, String description) {
        
        // 1. Update the User table
        user.setName(name); // This updates the "Welcome, Subarta Ghosh" part
        userRepository.save(user);

        // 2. Update the Employer table
        Employer employer = employerRepository.findById(user.getUserId())
                .orElseThrow(() -> new IllegalStateException("Employer profile not found."));
        
        employer.setCompanyName(companyName);
        employer.setLocation(location);
        employer.setWebsite(website);
        employer.setDescription(description);
        employerRepository.save(employer);
    }
}