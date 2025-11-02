package com.empexchng.empexchng.service;

import com.empexchng.empexchng.model.Application;
import com.empexchng.empexchng.model.Job;
import com.empexchng.empexchng.model.JobSeeker;
import com.empexchng.empexchng.model.User;
import com.empexchng.empexchng.repository.ApplicationRepository;
import com.empexchng.empexchng.repository.JobRepository;
import com.empexchng.empexchng.repository.JobSeekerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant; // Import this

@Service
public class ApplicationService {

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private JobSeekerRepository jobSeekerRepository;

    @Transactional
    public void applyForJob(Long jobId, User user) {
        String seekerId = user.getUserId();

        if (applicationRepository.existsByJob_JobIdAndSeeker_SeekerId(jobId, seekerId)) {
            throw new IllegalStateException("You have already applied for this job.");
        }

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid job ID."));
        
        JobSeeker seeker = jobSeekerRepository.findById(seekerId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid job seeker profile."));

        Application application = Application.builder()
                .job(job)
                .seeker(seeker)
                .status("PENDING")
                .build();

        applicationRepository.save(application);
    }
    
    // --- ADD THIS NEW METHOD ---
    @Transactional
    public void updateApplicationStatus(Long applicationId, String status) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid application ID."));
        
        // You could add more logic here, e.g., check if the user is the employer
        
        application.setStatus(status);
        application.setDecidedAt(Instant.now());
        applicationRepository.save(application);
    }
}