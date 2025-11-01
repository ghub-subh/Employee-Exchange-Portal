package com.empexchng.empexchng.controller;


import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.empexchng.empexchng.model.User;



@Controller
public class OAuthController {

    @GetMapping("/oauth2/success")
    public String success(@AuthenticationPrincipal OAuth2User oAuth2User, Model model) {
        String name = oAuth2User.getAttribute("name");
        String email = oAuth2User.getAttribute("email");
        String picture = oAuth2User.getAttribute("picture");

        model.addAttribute("name", name);
        model.addAttribute("email", email);
        model.addAttribute("photoUrl", picture);
        model.addAttribute("fromGoogle", true);

        model.addAttribute("user", new User());

        return "register"; // return the same Thymeleaf registration page
    }
}

