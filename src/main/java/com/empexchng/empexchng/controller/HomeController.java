package com.empexchng.empexchng.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        // Returns the "homepage.html" Thymeleaf template (src/main/resources/templates/home.html)
        return "homepage";
    }
}
