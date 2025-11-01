package com.empexchng.empexchng.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.empexchng.empexchng.model.User;
import com.empexchng.empexchng.repository.UserRepository;
import com.empexchng.empexchng.service.UserService;


@Controller
public class HomeController {

    @Autowired
    private UserRepository userRepository;

   @GetMapping("/")
public String home(@CookieValue(value = "ee_email", required = false) String emailCookie,
                   Model model) {
  if (emailCookie != null && !emailCookie.isBlank()) {
    // Optional: verify the email exists and fetch role
    String role = userRepository.getRoleByEmail(emailCookie);

    String target = switch (role) {
      case "ADMIN"      -> "/admin/panel";
      case "EMPLOYEE"   -> "/employee/panel";
      case "JOB_SEEKER" -> "/jobseeker/panel";
      default           -> "/login";
    };
    return "redirect:" + target;  // short-circuit to panel
  }

  // No cookie -> show the normal home page (or login/register UI)
  model.addAttribute("user", new User());
  return "homepage";
}


    @GetMapping("/login")
    public String login() {
        // Returns the "login.html" Thymeleaf template (src/main/resources/templates/login.html)
        return "login";
    }

  @GetMapping("/register")
  public String register(Model model) {
      model.addAttribute("user", new User());
    return "register";
  }
    
}
