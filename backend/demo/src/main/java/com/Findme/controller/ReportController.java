package com.Findme.controller;

import com.Findme.entity.LostItem;
import com.Findme.entity.User;
import com.Findme.service.GeminiService;
import com.Findme.service.LostItemService;
import com.Findme.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class ReportController {

    @Autowired
    private LostItemService lostItemService;

    @Autowired
    private GeminiService geminiService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/report")
    public ResponseEntity<Map<String, Object>> submitReport(
            @RequestParam("itemType") String itemType,
            @RequestParam("description") String description,
            @RequestParam("location") String location,
            @RequestParam("date") String date,
            @RequestParam("status") String status,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "image", required = false) MultipartFile image) {
        
        try {
            LostItem item = new LostItem();
            item.setItemType(itemType);
            item.setDescription(description);
            item.setLocation(location);
            item.setDate(date);
            item.setStatus(status);

            String contactInfo = email != null ? email : "anonymous@example.com";
            if (email != null) {
                Optional<User> userOpt = userRepository.findByEmail(email);
                if (userOpt.isPresent()) {
                    User u = userOpt.get();
                    contactInfo = u.getFullName() + " | " + u.getEmail() + (u.getPhone() != null ? " | " + u.getPhone() : "");
                }
            }
            item.setContactEmail(contactInfo);

            if (image != null && !image.isEmpty()) {
                byte[] bytes = image.getBytes();
                String base64Image = Base64.getEncoder().encodeToString(bytes);
                item.setImageBase64(base64Image);
            }

            LostItem savedItem = lostItemService.createLostItem(item);

            List<Map<String, Object>> matchedItems = new ArrayList<>();
            String matchMessage = "";

            if ("Found".equalsIgnoreCase(status)) {
                List<LostItem> allLostItems = lostItemService.findByStatus("Lost").stream()
                                                    .filter(i -> !i.isResolved())
                                                    .collect(Collectors.toList());
                String matchResult = geminiService.matchFoundItem(savedItem, allLostItems);
                
                if (matchResult != null && !matchResult.equals("NONE") && !matchResult.contains("Error")) {
                    try {
                        Long matchedId = Long.parseLong(matchResult.trim());
                        Optional<LostItem> matchedLostItem = lostItemService.getLostItemById(matchedId);
                        if (matchedLostItem.isPresent() && !matchedLostItem.get().isResolved()) {
                            Map<String, Object> matchInfo = new HashMap<>();
                            matchInfo.put("id", matchedLostItem.get().getId());
                            matchInfo.put("itemType", matchedLostItem.get().getItemType());
                            matchInfo.put("description", matchedLostItem.get().getDescription());
                            matchInfo.put("contactEmail", matchedLostItem.get().getContactEmail());
                            matchInfo.put("status", matchedLostItem.get().getStatus());
                            matchedItems.add(matchInfo);
                            matchMessage = "We found a potential match for this item! Owner will be notified.";
                        }
                    } catch (NumberFormatException e) {
                        System.err.println("Could not parse match ID from Gemini: " + matchResult);
                    }
                }
            }

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Report submitted successfully. " + matchMessage);
            response.put("matchedItems", matchedItems);
            response.put("submittedItem", savedItem);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/recent")
    public ResponseEntity<List<LostItem>> getRecentItems() {
        List<LostItem> allItems = lostItemService.getAllLostItems().stream()
                                    .filter(item -> !item.isResolved())
                                    .collect(Collectors.toList());
        Collections.reverse(allItems);
        return ResponseEntity.ok(allItems);
    }

    @GetMapping("/user/reports")
    public ResponseEntity<List<LostItem>> getUserReports(@RequestParam(value = "email", required = false) String email) {
        List<LostItem> allItems = lostItemService.getAllLostItems();
        if (email != null) {
            allItems = allItems.stream()
                .filter(item -> item.getContactEmail() != null && item.getContactEmail().contains(email))
                .collect(Collectors.toList());
        }
        Collections.reverse(allItems);
        return ResponseEntity.ok(allItems);
    }

    @PutMapping("/user/reports/{id}/resolve")
    public ResponseEntity<Map<String, String>> resolveReport(@PathVariable Long id) {
        Optional<LostItem> itemOpt = lostItemService.getLostItemById(id);
        if (itemOpt.isPresent()) {
            LostItem item = itemOpt.get();
            item.setResolved(true);
            lostItemService.updateLostItem(id, item);
            return ResponseEntity.ok(Map.of("message", "Item marked as resolved."));
        }
        return ResponseEntity.notFound().build();
    }
}