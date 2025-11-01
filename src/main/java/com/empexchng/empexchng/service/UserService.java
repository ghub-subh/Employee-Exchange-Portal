package com.empexchng.empexchng.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.empexchng.empexchng.model.User;
import com.empexchng.empexchng.repository.UserRepository;



@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Create a password encoder instance
    @Autowired
    private PasswordEncoder passwordEncoder;

    public String registerUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            return "Email already registered!";
        }

        if (userRepository.existsByUserId(user.getUserId())) {
            return "User ID already registered!";
        }

        // If password exists, encode it before saving
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        // If userId not present, generate one
        if (user.getUserId() == null || user.getUserId().isEmpty()) {
            user.setUserId(generateUserId(user.getRole()));
        }

        userRepository.save(user);
        return "User registered successfully!";
    }
    public String loginUser(User user) {
    User existingUser = userRepository.findByEmail(user.getEmail());
    if (existingUser == null) {
        throw new IllegalArgumentException("User not found");
    }
    if (!passwordEncoder.matches(user.getPassword(), existingUser.getPassword())) {
        throw new IllegalArgumentException("Invalid credentials");
    }
    // Return the persisted role, never the form’s null role
    return existingUser.getRole(); // "ADMIN" | "EMPLOYEE" | "JOB_SEEKER"
}


    private String generateUserId(String role) {
        return role.charAt(0) + String.format("%04d", userRepository.count() + 1);
    }
}
