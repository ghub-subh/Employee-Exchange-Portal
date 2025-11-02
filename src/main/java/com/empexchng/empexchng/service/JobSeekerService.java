package com.empexchng.empexchng.service;

import com.empexchng.empexchng.model.JobSeeker;
import com.empexchng.empexchng.model.User;
import com.empexchng.empexchng.repository.JobSeekerRepository;
import com.empexchng.empexchng.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class JobSeekerService {

    @Autowired
    private JobSeekerRepository jobSeekerRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public void updateProfile(User user, String name, String location, String skills, BigDecimal experience) {
        
        // 1. Update the User table
        user.setName(name);
        userRepository.save(user);

        // 2. Update the JobSeeker table
        JobSeeker seeker = jobSeekerRepository.findById(user.getUserId())
                .orElseThrow(() -> new IllegalStateException("Job seeker profile not found."));
        
        seeker.setLocation(location);
        seeker.setSkillsText(skills);
        seeker.setExperienceYears(experience);
        jobSeekerRepository.save(seeker);
    }
}