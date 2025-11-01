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
  String role = userService.loginUser(user);

  // For localhost (HTTP), do not set Secure or the cookie won’t be sent back.
  Cookie emailCookie = new Cookie("ee_email", user.getEmail());
  emailCookie.setPath("/");
  //emailCookie.setHttpOnly(true);
  emailCookie.setMaxAge(7 * 24 * 60 * 60);
  response.addCookie(emailCookie);

  String target = switch (role) {
    case "ADMIN" -> "/admin/panel";
    case "EMPLOYER" -> "/employer/dashboard";
    case "JOB_SEEKER" -> "/jobseeker/panel";
    default -> "/";
  };
  return "redirect:" + target;
}

@PostMapping("/logout")
public String logout(HttpServletResponse resp, RedirectAttributes ra) {
  jakarta.servlet.http.Cookie cookie = new jakarta.servlet.http.Cookie("ee_email", "");
  cookie.setPath("/");
  cookie.setMaxAge(0);         // expire immediately
  cookie.setHttpOnly(true);
  resp.addCookie(cookie);
  ra.addFlashAttribute("message", "Logged out.");
  return "redirect:/";
}


}