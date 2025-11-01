package com.empexchng.empexchng.controller;

import  java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.empexchng.empexchng.model.User;
import com.empexchng.empexchng.service.UserService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;




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
                           OAuth2AuthenticationToken authentication,RedirectAttributes ra) {
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

    String message = userService.registerUser(user);
    model.addAttribute("message", message);
    model.addAttribute("user", new User());
if (message.contains("successfully")) {
    ra.addFlashAttribute("message", message);
    return "redirect:/login";            // redirect to GET /login
  } else {
    ra.addFlashAttribute("message", message);
    ra.addFlashAttribute("user", user);  // repopulate fields
    return "redirect:/register";         // redirect to GET /register
  }
}

@PostMapping("/login")
public String loginUser(@ModelAttribute("user") User user,
                        HttpServletResponse response,
                        RedirectAttributes ra) {
  // 1) Authenticate (returns role or throws)
  String role = userService.loginUser(user); // e.g., "ADMIN"/"JOB_SEEKER"/"EMPLOYEE"

  // 2) On success: set a cookie with the email (session cookie shown here)
  Cookie emailCookie = new Cookie("ee_email", user.getEmail());
  emailCookie.setPath("/");              // send with all requests
  emailCookie.setHttpOnly(true);         // not accessible from JS
  emailCookie.setSecure(true);        // enable if using HTTPS
  emailCookie.setMaxAge(7 * 24 * 60 * 60); // uncomment for persistent cookie
  response.addCookie(emailCookie);       // adds Set-Cookie header [web:388][web:383]

  // 3) Redirect by role (PRG)
  String target = switch (role) {
    case "ADMIN"     -> "/admin/panel";
    case "EMPLOYEE"  -> "/employee/panel";
    case "JOB_SEEKER"-> "/jobseeker/panel";
    default          -> "/";              // fallback
  };
  return "redirect:" + target;            // do not return a view name here [web:191][web:246]
}

}