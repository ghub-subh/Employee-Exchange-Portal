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
public String home(@CookieValue(value="ee_email", required=false) String email,
                   Model model) {
  if (email != null && !email.isBlank()) {
    String role = userRepository.getRoleByEmail(email);
    String target = switch (role) {
      case "ADMIN" -> "/admin/panel";
      case "EMPLOYER" -> "/employer/dashboard";
      case "JOB_SEEKER" -> "/jobseeker/panel";
      default -> "/login";
    };
    return "redirect:" + target;
  }
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

  @GetMapping("/register")
    public String register(Model model)
    {

      model.addAttribute("user", new  User());
      return "register";

    }
  }
  


    

