package com.empexchng.empexchng.model;

public class VacancyRequest {
    private Long id;
    private String userName;
    private String userEmail;
    private String userProfilePicture;
    private String vacancyTitle;
    private String resume;

    // No-args constructor (Good practice)
    public VacancyRequest() {}

    // The 6-argument constructor that your controller needs
    public VacancyRequest(Long id, String userName, String userEmail, String userProfilePicture, String vacancyTitle, String resume) {
        this.id = id;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userProfilePicture = userProfilePicture;
        this.vacancyTitle = vacancyTitle;
        this.resume = resume;
    }

    // --- GETTERS ---
    // Thymeleaf needs these to read the data.
    public Long getId() { return id; }
    public String getUserName() { return userName; }
    public String getUserEmail() { return userEmail; }
    public String getUserProfilePicture() { return userProfilePicture; }
    public String getVacancyTitle() { return vacancyTitle; }
    public String getResume() { return resume; }

    // --- SETTERS --- (Good practice to have)
    public void setId(Long id) { this.id = id; }
    public void setUserName(String userName) { this.userName = userName; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }
    public void setVacancyTitle(String vacancyTitle) { this.vacancyTitle = vacancyTitle; }
    public void setResume(String resume) { this.resume = resume; }
    public void setUserProfilePicture(String userProfilePicture) { this.userProfilePicture = userProfilePicture; }
}