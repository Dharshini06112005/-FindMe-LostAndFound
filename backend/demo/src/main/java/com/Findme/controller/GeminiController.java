package com.Findme.controller;

import com.Findme.service.GeminiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/ai")
@CrossOrigin(origins = "*")
public class GeminiController {
    
    @Autowired
    private GeminiService geminiService;
    
    // POST - Send a general prompt to Gemini
    @PostMapping("/chat")
    public ResponseEntity<Map<String, String>> chatWithGemini(@RequestBody Map<String, String> request) {
        try {
            String prompt = request.get("prompt");
            if (prompt == null || prompt.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Prompt is required"));
            }
            
            String response = geminiService.getGeminiResponse(prompt);
            return ResponseEntity.ok(Map.of("response", response));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
        }
    }
    
    // GET - Simple test endpoint for browser
    @GetMapping("/test")
    public ResponseEntity<Map<String, String>> testGemini() {
        try {
            String response = geminiService.getGeminiResponse("Hello! Can you help me with finding lost items?");
            return ResponseEntity.ok(Map.of("response", response));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
        }
    }
    
    // POST - Get suggestions for lost item
    @PostMapping("/suggest")
    public ResponseEntity<Map<String, String>> getLostItemSuggestions(@RequestBody Map<String, String> request) {
        try {
            String itemName = request.get("itemName");
            String location = request.get("location");
            
            if (itemName == null || location == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "itemName and location are required"));
            }
            
            String response = geminiService.getLostItemSuggestion(itemName, location);
            return ResponseEntity.ok(Map.of("response", response));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
        }
    }
    
    // GET - Get general help
    @GetMapping("/help")
    public ResponseEntity<Map<String, String>> getGeneralHelp(@RequestParam String query) {
        try {
            String response = geminiService.getGeneralHelp(query);
            return ResponseEntity.ok(Map.of("response", response));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
        }
    }
} 