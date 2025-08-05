package com.Findme.controller;

import com.Findme.entity.LostItem;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class ReportController {

    @PostMapping("/report")
    public ResponseEntity<Map<String, Object>> submitReport(
            @RequestParam("itemType") String itemType,
            @RequestParam("description") String description,
            @RequestParam("location") String location,
            @RequestParam("date") String date,
            @RequestParam("status") String status,
            @RequestParam(value = "image", required = false) MultipartFile image) {
        
        // Log the received data
        System.out.println("Received report: " + itemType + ", " + description + ", " + location + ", " + date + ", " + status);
        
        // For now, return mock matched items based on the item type
        List<Map<String, Object>> matchedItems = new ArrayList<>();
        
        // Add some mock matched items based on the search
        if (itemType.toLowerCase().contains("phone") || description.toLowerCase().contains("phone")) {
            matchedItems.add(Map.of(
                "id", 1,
                "itemName", "Phone",
                "description", "iPhone 13, black case",
                "location", "Library",
                "contact", "john@example.com",
                "status", "Lost",
                "createdAt", new Date()
            ));
        }
        
        if (itemType.toLowerCase().contains("laptop") || description.toLowerCase().contains("laptop")) {
            matchedItems.add(Map.of(
                "id", 2,
                "itemName", "Laptop",
                "description", "MacBook Pro, silver",
                "location", "Computer Lab",
                "contact", "alice@example.com",
                "status", "Lost",
                "createdAt", new Date()
            ));
        }
        
        if (itemType.toLowerCase().contains("keys") || description.toLowerCase().contains("keys")) {
            matchedItems.add(Map.of(
                "id", 3,
                "itemName", "Keys",
                "description", "Car keys with keychain",
                "location", "Cafeteria",
                "contact", "jane@example.com",
                "status", "Found",
                "createdAt", new Date()
            ));
        }
        
        // If no specific matches, add some general items
        if (matchedItems.isEmpty()) {
            matchedItems.add(Map.of(
                "id", 4,
                "itemName", "Wallet",
                "description", "Brown leather wallet",
                "location", "Parking Lot",
                "contact", "bob@example.com",
                "status", "Found",
                "createdAt", new Date()
            ));
        }

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Report submitted successfully");
        response.put("matchedItems", matchedItems);
        response.put("submittedItem", Map.of(
            "itemType", itemType,
            "description", description,
            "location", location,
            "date", date,
            "status", status
        ));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/recent")
    public ResponseEntity<List<Map<String, Object>>> getRecentItems() {
        // Return mock recent items
        List<Map<String, Object>> recentItems = Arrays.asList(
            Map.of(
                "id", 1,
                "itemName", "Laptop",
                "description", "MacBook Pro, silver",
                "location", "Computer Lab",
                "contact", "alice@example.com",
                "status", "Lost",
                "createdAt", new Date()
            ),
            Map.of(
                "id", 2,
                "itemName", "Wallet",
                "description", "Brown leather wallet",
                "location", "Parking Lot",
                "contact", "bob@example.com",
                "status", "Found",
                "createdAt", new Date()
            ),
            Map.of(
                "id", 3,
                "itemName", "Backpack",
                "description", "Blue Jansport backpack",
                "location", "Student Center",
                "contact", "charlie@example.com",
                "status", "Lost",
                "createdAt", new Date()
            )
        );
        return ResponseEntity.ok(recentItems);
    }

    @GetMapping("/user/reports")
    public ResponseEntity<List<Map<String, Object>>> getUserReports() {
        // Return mock user reports
        List<Map<String, Object>> userReports = Arrays.asList(
            Map.of(
                "id", 1,
                "itemName", "Phone",
                "description", "Samsung Galaxy S21",
                "location", "Library",
                "contact", "user@example.com",
                "status", "Lost",
                "createdAt", new Date()
            ),
            Map.of(
                "id", 2,
                "itemName", "Watch",
                "description", "Apple Watch Series 7",
                "location", "Gym",
                "contact", "user@example.com",
                "status", "Found",
                "createdAt", new Date()
            )
        );
        return ResponseEntity.ok(userReports);
    }
} 