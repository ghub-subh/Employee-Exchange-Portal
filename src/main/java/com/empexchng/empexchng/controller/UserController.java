package com.empexchng.empexchng.controller;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.empexchng.empexchng.model.User;
import com.empexchng.empexchng.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String showRegisterForm(Model model, OAuth2AuthenticationToken authentication) {
        User user = new User();

        if (authentication != null) {
            Map<String, Object> attrs = authentication.getPrincipal().getAttributes();
            user.setName((String) attrs.get("name"));
            user.setEmail((String) attrs.get("email"));
            user.setPhotoUrl((String) attrs.get("picture"));
            model.addAttribute("fromGoogle", true);
        } else {
            model.addAttribute("fromGoogle", false);
        }

        model.addAttribute("user", user);
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user,
                               Model model,
                               OAuth2AuthenticationToken authentication, RedirectAttributes ra) {
        
        if (authentication != null) {
            Map<String, Object> attrs = authentication.getPrincipal().getAttributes();
            if (user.getEmail() == null || user.getEmail().isEmpty()) {
                user.setEmail((String) attrs.get("email"));
            }
            if (user.getName() == null || user.getName().isEmpty()) {
                user.setName((String) attrs.get("name"));
            }
            if (user.getPhotoUrl() == null) {
                user.setPhotoUrl((String) attrs.get("picture"));
            }
        }

        String message = "";
        try {
            message = userService.registerUser(user);
        } catch (Exception e) {
            e.printStackTrace(); 
            message = "Error during registration. Please check console.";
        }
        
        if (message.contains("successfully")) {
            ra.addFlashAttribute("message", message);
            return "redirect:/login";
        } else {
            ra.addFlashAttribute("message", message);
            ra.addFlashAttribute("user", user);
            return "redirect:/register";
        }
    }
}