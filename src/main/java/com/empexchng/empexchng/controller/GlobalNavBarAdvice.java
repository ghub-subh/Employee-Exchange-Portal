package com.empexchng.empexchng.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.CookieValue;

@ControllerAdvice
public class GlobalNavBarAdvice {
  @ModelAttribute
  public void addNavbarState(Model model,
      @CookieValue(value = "ee_email", required = false) String emailCookie) {
    boolean loggedIn = emailCookie != null && !emailCookie.isBlank();
    model.addAttribute("loggedIn", loggedIn);
    model.addAttribute("email", emailCookie);
  }
}
