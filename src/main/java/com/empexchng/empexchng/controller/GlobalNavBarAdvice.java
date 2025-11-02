package com.empexchng.empexchng.controller;

import com.empexchng.empexchng.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalNavBarAdvice {

    @ModelAttribute
    public void addNavbarState(Model model, Authentication authentication, @AuthenticationPrincipal User user) {
        boolean loggedIn = (authentication != null && authentication.isAuthenticated());
        model.addAttribute("loggedIn", loggedIn);

        if (loggedIn && user != null) {
            model.addAttribute("loggedInUser", user);
        }
    }
}