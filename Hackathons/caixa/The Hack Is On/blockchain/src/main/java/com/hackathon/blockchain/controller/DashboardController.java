package com.hackathon.blockchain.controller;

import com.hackathon.blockchain.model.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hackathon.blockchain.model.User;


@RestController
@RequestMapping("/api")
public class DashboardController {

    @GetMapping("/dashboard")
    public String getUserDashboard(@AuthenticationPrincipal User user) {
        if (user == null) {
            return "You are not authenticated";
        }

        return "Welcome to your dashboard, " + user.getUsername() + "!\n" +
               "Your registered email is: " + user.getEmail();
    }
}