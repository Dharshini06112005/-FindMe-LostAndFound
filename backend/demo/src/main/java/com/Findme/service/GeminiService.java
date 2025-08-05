package com.Findme.service;

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
        String prompt = String.format(
            "I lost a %s in %s. Can you provide some helpful suggestions for finding it? " +
            "Please give practical advice and tips for locating lost items in this area.",
            itemName, location
        );
        return getGeminiResponse(prompt);
    }
    
    public String getGeneralHelp(String query) {
        String prompt = String.format(
            "You are a helpful assistant for a lost and found system. " +
            "A user is asking: %s. Please provide helpful and practical advice.",
            query
        );
        return getGeminiResponse(prompt);
    }
} 