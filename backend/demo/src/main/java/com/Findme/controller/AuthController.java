package com.Findme.controller;

import com.Findme.entity.User;
import com.Findme.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/signup")
    public ResponseEntity<Map<String, Object>> signup(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        if (userRepository.findByEmail(email).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Email already registered"));
        }

        User user = new User();
        user.setFullName(request.get("fullName"));
        user.setEmail(email);
        user.setPassword(request.get("password")); // Storing plain text for simplicity, in prod use BCrypt
        user.setPhone(request.get("phone"));

        User savedUser = userRepository.save(user);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "User registered successfully");
        response.put("token", "mock-jwt-token-" + savedUser.getId());
        response.put("user", Map.of(
            "id", savedUser.getId(),
            "name", savedUser.getFullName(),
            "email", savedUser.getEmail(),
            "phone", savedUser.getPhone() != null ? savedUser.getPhone() : ""
        ));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String password = request.get("password");

        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty() || !userOpt.get().getPassword().equals(password)) {
            return ResponseEntity.badRequest().body(Map.of("message", "Invalid credentials"));
        }

        User user = userOpt.get();

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Login successful");
        response.put("token", "mock-jwt-token-" + user.getId());
        response.put("user", Map.of(
            "id", user.getId(),
            "name", user.getFullName(),
            "email", user.getEmail(),
            "phone", user.getPhone() != null ? user.getPhone() : ""
        ));
        return ResponseEntity.ok(response);
    }
}