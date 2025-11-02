package com.empexchng.empexchng.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.empexchng.empexchng.model.User;
import com.empexchng.empexchng.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // We find the user by email, since that's what we use to log in
        User user = userRepository.findByEmail(email); 
        if (user == null) {
            throw new UsernameNotFoundException("No user found with email: " + email);
        }
        
        // Return the user object. Spring Security will handle the password check.
        return user;
    }
}