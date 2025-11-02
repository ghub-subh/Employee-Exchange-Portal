# EmpExchng: Online Employment Exchange Portal

`EmpExchng` is a comprehensive, three-tier web application that simulates a real-world online job board. It connects Job Seekers with Employers and is fully managed by an Admin.

The project's core feature is a **custom admin-approval workflow**, which ensures that both new user registrations and new job postings must be manually verified by an administrator before they become active on the site.


## Key Features

* **Role-Based Security:** Secure, role-specific access for three distinct user roles:
    * **`ADMIN`**: Can approve/reject new users and new job postings.
    * **`EMPLOYER`**: Can post new jobs (vacancies) and manage (approve/reject) applications for those jobs.
    * **`JOB_SEEKER`**: Can browse and apply for approved jobs and manage their own profile.
* **Admin Approval Workflow:**
    * New users (Employers & Job Seekers) are saved with `is_approved = false` and cannot log in until an Admin approves them.
    * New jobs are saved with `is_approved = false` and are not visible to Job Seekers until an Admin approves them.
* **Unified Authentication:** A single, secure system managed by Spring Security 6.
    * Handles both custom form login (email/password).
    * Integrates Google OAuth2 for registration and login.
    * Users registering with Google are still required to create a local password.
* **Full Application Lifecycle:**
    1.  An **Employer** posts a job.
    2.  An **Admin** approves the job.
    3.  A **Job Seeker** sees the job and applies for it.
    4.  The **Employer** sees the "PENDING" application and can "Approve" or "Reject" it.
    5.  The **Job Seeker** can see their application status on their "My Applications" page.
* **Dynamic UI:**
    * Built with Thymeleaf and styled with Tailwind CSS.
    * Includes dynamic "initials" avatars that generate automatically if no profile picture is set.
    * Conditionally renders buttons (e.g., shows "Already Applied" if a user has applied for a job).


## Tech Stack

* **Backend:** Java 17, Spring Boot 3.x
* **Security:** Spring Security 6.x (Form Login & OAuth2)
* **Database:** MySQL 8
* **Data Access:** Spring Data JPA (Hibernate)
* **Frontend:** Thymeleaf, Tailwind CSS
* **Build:** Maven
