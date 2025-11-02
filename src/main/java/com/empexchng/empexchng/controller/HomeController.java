package com.empexchng.empexchng.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.empexchng.empexchng.model.User;
import com.empexchng.empexchng.repository.UserRepository;


@Controller
public class HomeController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/post-login-redirect")
    public String postLoginRedirect(Authentication authentication) {
        if (authentication == null) {
            return "redirect:/login";
        }

        String role = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse("NONE");

        return switch (role) {
            case "ROLE_ADMIN" -> "redirect:/admin/panel";
            case "ROLE_EMPLOYER" -> "redirect:/employer/dashboard";
            case "ROLE_JOB_SEEKER" -> "redirect:/jobseeker/panel";
            default -> "redirect:/login?error=true";
        };
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("user", new User());
        return "homepage";
    }

    @GetMapping("/login")
    public String showLogin(Model model) {
        if (!model.containsAttribute("user")) {
            model.addAttribute("user", new User());
        }
        return "login";
    }
}