package com.Findme.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/test")
    public String test() {
        return "NovaTrace Backend is running successfully!";
    }

    @GetMapping("/")
    public String home() {
        return "Welcome to NovaTrace - Neighbourhood lost and found application";
    }
} 