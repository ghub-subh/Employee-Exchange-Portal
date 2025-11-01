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
        if (existingUser != null) {
            if (passwordEncoder.matches(user.getPassword(), existingUser.getPassword())) {
                return user.getRole();
            } else {
                return "Invalid password!";
            }
        } else {
            return "User not found!";
        }
    }

    private String generateUserId(String role) {
        return role.charAt(0) + String.format("%04d", userRepository.count() + 1);
    }
}
