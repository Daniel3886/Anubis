package com.anubis.backend.auth.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {


    @GetMapping("/dashboard")
    public String dashboard(@AuthenticationPrincipal OAuth2User user) {
        if(user != null) {
            return "Hello, OAuth2 user: " + user;
        }

        return "Hello Authenticated User!";
    }

    @GetMapping("/public")
    public String publicEndpoint(){
        return "Public endpoint, anyone can see this";
    }
}
