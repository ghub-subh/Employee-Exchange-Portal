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

import java.time.Instant;

@Service
public class ApplicationService {

    // Dependencies using Field Injection with @Autowired
    @Autowired
    private ApplicationRepository applicationRepository;
    
    @Autowired
    private JobRepository jobRepository;
    
    @Autowired
    private JobSeekerRepository jobSeekerRepository;
    
    @Autowired
    private EmailService emailService;

    // The explicit constructor has been removed in favor of field injection.
    
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
        
        // --- 1. Send Confirmation Email to Job Seeker ---
        sendApplicationConfirmationEmail(job, user);
        
        // --- 2. Send Notification Email to Employer ---
        sendNewApplicationNotificationEmail(job, user);
    }
    
    @Transactional
    public void updateApplicationStatus(Long applicationId, String status) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid application ID."));
        
        String oldStatus = application.getStatus();
        
        application.setStatus(status);
        application.setDecidedAt(Instant.now());
        Application savedApplication = applicationRepository.save(application);

        // --- 3. Send Status Update Email to Job Seeker ---
        // Only send if the status has changed AND it's an acceptance or rejection
        if (!oldStatus.equals(status) && ("ACCEPTED".equals(status) || "REJECTED".equals(status))) {
            sendApplicationStatusUpdateEmail(savedApplication);
        }
    }

    // =========================================================================
    // Private Email Helper Methods
    // =========================================================================

    private void sendApplicationConfirmationEmail(Job job, User seekerUser) {
        String subject = "Application Confirmation: " + job.getTitle() + " at " + job.getEmployer().getCompanyName();
        String body = "<p>Dear " + seekerUser.getName() + ",</p>"
                    + "<p>This confirms your application for the position of <b>" + job.getTitle() + "</b> at <b>" + job.getEmployer().getCompanyName() + "</b>.</p>"
                    + "<p>Your application is currently PENDING review. We will notify you of any status updates.</p>"
                    + "<p>Thank you, The EmpExchang Team.</p>";
        emailService.sendEmail(seekerUser.getEmail(), subject, body);
    }
    
    private void sendNewApplicationNotificationEmail(Job job, User seekerUser) {
        String employerEmail = job.getEmployer().getUser().getEmail();
        String employerName = job.getEmployer().getUser().getName();
        String subject = "New Job Application for: " + job.getTitle();
        String body = "<p>Dear " + employerName + ",</p>"
                    + "<p>A new application has been received for the position of <b>" + job.getTitle() + "</b>.</p>"
                    + "<p>Applicant: " + seekerUser.getName() + " (" + seekerUser.getEmail() + ")</p>"
                    + "<p>Please log in to the EmpExchang portal to review the application.</p>"
                    + "<p>Regards, The EmpExchang Team.</p>";
        emailService.sendEmail(employerEmail, subject, body);
    }

    private void sendApplicationStatusUpdateEmail(Application application) {
        String seekerName = application.getSeeker().getUser().getName();
        String seekerEmail = application.getSeeker().getUser().getEmail();
        String jobTitle = application.getJob().getTitle();
        String companyName = application.getJob().getEmployer().getCompanyName();
        String status = application.getStatus();

        String subject;
        String body;

        if ("ACCEPTED".equals(status)) {
            subject = "Congratulations! Your Application for " + jobTitle + " was Accepted";
            body = "<p>Dear " + seekerName + ",</p>"
                 + "<p>We are delighted to inform you that your application for the <b>" + jobTitle + "</b> position at <b>" + companyName + "</b> has been **ACCEPTED**! </p>"
                 + "<p>The employer will be in touch shortly regarding the next steps in the hiring process.</p>"
                 + "<p>Congratulations! The EmpExchang Team.</p>";
        } else if ("REJECTED".equals(status)) {
            subject = "Update on Your Application for " + jobTitle;
            body = "<p>Dear " + seekerName + ",</p>"
                 + "<p>Thank you for your interest in the <b>" + jobTitle + "</b> position at <b>" + companyName + "</b>.</p>"
                 + "<p>After careful review, we regret to inform you that your application has been **REJECTED** at this time.</p>"
                 + "<p>We wish you the best in your future job applications.</p>"
                 + "<p>Regards, The EmpExchang Team.</p>";
        } else {
            return; // Should not happen if called correctly
        }

        emailService.sendEmail(seekerEmail, subject, body);
    }
}
