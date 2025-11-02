package com.empexchng.empexchng.service;

import com.empexchng.empexchng.model.Employer;
import com.empexchng.empexchng.model.Job;
import com.empexchng.empexchng.model.User;
import com.empexchng.empexchng.repository.EmployerRepository;
import com.empexchng.empexchng.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class JobService {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private EmployerRepository employerRepository;

    @Transactional
    public Job createJob(String title, String description, String location, String skills, User employerUser) {
        
        Employer employer = employerRepository.findByUser_Email(employerUser.getEmail());
        if (employer == null) {
            throw new IllegalStateException("No employer profile found for this user.");
        }

        Job job = Job.builder()
                .employer(employer)
                .title(title)
                .description(description)
                .location(location)
                .skillsRequired(skills)
                .isActive(true)
                .isApproved(false) // <-- Set approval to false
                .build();

        return jobRepository.save(job);
    }
    
    @Transactional
    public void approveJob(Long jobId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid job ID: " + jobId));
        job.setIsApproved(true);
        jobRepository.save(job);
    }
}