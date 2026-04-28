package com.Findme.service;

import com.Findme.entity.LostItem;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GeminiService {
    
    @Value("${spring.ai.openai.api-key}")
    private String apiKey;
    
    @Value("${spring.ai.openai.base-url}")
    private String baseUrl;
    
    private final RestTemplate restTemplate = new RestTemplate();
    
    public String matchFoundItem(LostItem foundItem, List<LostItem> lostItems) {
        if (lostItems.isEmpty()) return "No lost items to match.";
        
        try {
            String url = baseUrl + "/chat/completions";
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + apiKey);
            
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", "gemini-1.5-flash");
            
            StringBuilder promptBuilder = new StringBuilder();
            promptBuilder.append("A user found an item with the following details:\n");
            promptBuilder.append("Type: ").append(foundItem.getItemType()).append("\n");
            promptBuilder.append("Description: ").append(foundItem.getDescription()).append("\n");
            promptBuilder.append("Location: ").append(foundItem.getLocation()).append("\n\n");
            promptBuilder.append("Here is a list of items currently reported as lost:\n");
            
            for (LostItem lost : lostItems) {
                promptBuilder.append("ID: ").append(lost.getId()).append("\n");
                promptBuilder.append("Type: ").append(lost.getItemType()).append("\n");
                promptBuilder.append("Description: ").append(lost.getDescription()).append("\n");
                promptBuilder.append("Location: ").append(lost.getLocation()).append("\n");
                promptBuilder.append("---\n");
            }
            
            promptBuilder.append("Does the found item match ANY of the lost items? If there is a high probability of a match based on the description and type, return ONLY the ID of the matched lost item. If no match is found, return exactly 'NONE'. Do not include any other text.");

            List<Object> contentList = new ArrayList<>();
            Map<String, String> textContent = new HashMap<>();
            textContent.put("type", "text");
            textContent.put("text", promptBuilder.toString());
            contentList.add(textContent);

            if (foundItem.getImageBase64() != null && !foundItem.getImageBase64().isEmpty()) {
                Map<String, Object> imageContent = new HashMap<>();
                imageContent.put("type", "image_url");
                Map<String, String> imageUrl = new HashMap<>();
                imageUrl.put("url", "data:image/jpeg;base64," + foundItem.getImageBase64());
                imageContent.put("image_url", imageUrl);
                contentList.add(imageContent);
            }

            Map<String, Object> message = new HashMap<>();
            message.put("role", "user");
            message.put("content", contentList);
            
            List<Map<String, Object>> messages = new ArrayList<>();
            messages.add(message);
            requestBody.put("messages", messages);
            requestBody.put("max_tokens", 50);
            
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, request, Map.class);
            
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                Map<String, Object> responseBody = response.getBody();
                if (responseBody.containsKey("choices")) {
                    List<Map<String, Object>> choices = (List<Map<String, Object>>) responseBody.get("choices");
                    if (!choices.isEmpty()) {
                        Map<String, Object> choice = choices.get(0);
                        Map<String, Object> messageResponse = (Map<String, Object>) choice.get("message");
                        return (String) messageResponse.get("content");
                    }
                }
            }
            return "NONE";
        } catch (Exception e) {
            System.err.println("Gemini Error: " + e.getMessage());
            return "NONE";
        }
    }

    public String getGeminiResponse(String prompt) {
        try {
            String url = baseUrl + "/chat/completions";
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + apiKey);
            
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", "gemini-1.5-flash");
            
            Map<String, String> message = new HashMap<>();
            message.put("role", "user");
            message.put("content", prompt);
            
            List<Map<String, String>> messages = new ArrayList<>();
            messages.add(message);
            requestBody.put("messages", messages);
            requestBody.put("max_tokens", 1000);
            
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
            
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, request, Map.class);
            
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                Map<String, Object> responseBody = response.getBody();
                if (responseBody.containsKey("choices")) {
                    List<Map<String, Object>> choices = (List<Map<String, Object>>) responseBody.get("choices");
                    if (!choices.isEmpty()) {
                        Map<String, Object> choice = choices.get(0);
                        Map<String, Object> messageResponse = (Map<String, Object>) choice.get("message");
                        return (String) messageResponse.get("content");
                    }
                }
            }
            return "Error: Unable to parse response from Gemini AI";
        } catch (Exception e) {
            return "Error: Unable to get response from Gemini AI. " + e.getMessage();
        }
    }
    
    public String getLostItemSuggestion(String itemName, String location) {
        return getGeminiResponse("I lost a " + itemName + " in " + location + ". Can you provide some helpful suggestions for finding it?");
    }
    
    public String getGeneralHelp(String query) {
        return getGeminiResponse("You are a helpful assistant for a lost and found system. A user is asking: " + query);
    }
} 