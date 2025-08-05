package com.Findme.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @PostMapping("/signup")
    public ResponseEntity<Map<String, Object>> signup(@RequestBody Map<String, String> request) {
        // For now, return a mock response
        Map<String, Object> response = new HashMap<>();
        response.put("message", "User registered successfully");
        response.put("token", "mock-jwt-token-" + System.currentTimeMillis());
        response.put("user", Map.of(
            "id", 1,
            "name", request.get("fullName"),
            "email", request.get("email")
        ));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> request) {
        // For now, return a mock response
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Login successful");
        response.put("token", "mock-jwt-token-" + System.currentTimeMillis());
        response.put("user", Map.of(
            "id", 1,
            "name", "Test User",
            "email", request.get("email")
        ));
        return ResponseEntity.ok(response);
    }
} 