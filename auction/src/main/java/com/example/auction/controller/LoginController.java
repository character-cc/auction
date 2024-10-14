package com.example.auction.controller;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.core.Authentication;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login(@RequestParam(value = "error" , required = false) String error , Model model , Authentication authentication){
        if (authentication != null && authentication.isAuthenticated()) {
            return "redirect:/home";
        }
        if(error != null){
            model.addAttribute("error","Invalid username or password. Please try again.");
        }
        return "login.html";
    }
}
