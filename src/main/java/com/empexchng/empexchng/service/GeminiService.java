package com.empexchng.empexchng.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@Service
public class GeminiService {

    // The REST endpoint for generating content
    private static final String API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent";

    private final RestTemplate restTemplate;
    private final String apiKey;
    private final ObjectMapper objectMapper = new ObjectMapper();

    // System instruction is now prepended to the user message
    private static final String SYSTEM_INSTRUCTION = 
        "You are a professional, specialized Job Search and Career Assistant. Your responses must be strictly limited to topics related to job hunting, resume building, interview preparation, salary negotiation, and career advice. If the user asks a question unrelated to jobs or careers, politely decline and state that you are only an employment assistant. Keep your tone formal, helpful, and concise.\n\n*** Begin User Query ***\n\n";

    public GeminiService(@Value("${gemini.api.key}") String apiKey) {
        this.apiKey = apiKey;
        this.restTemplate = new RestTemplate();
    }

    public String generateJobResponse(String userMessage) throws Exception {
        
        // --- 1. CONSTRUCT COMBINED MESSAGE AND REQUEST JSON ---
        
        // Combine the system instruction with the user's message
        String combinedMessage = SYSTEM_INSTRUCTION + userMessage;

        // Escape quotes in the combined message to prevent JSON parsing issues.
        String escapedCombinedMessage = combinedMessage.replace("\"", "\\\"");

        // The request JSON now only contains the 'contents' array with the combined message.
        String requestJson = String.format("""
            {
              "contents": [{ "role": "user", "parts": [{ "text": "%s" }] }]
            }
            """, escapedCombinedMessage);
        
        // --------------------------------------------------------
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        // --- 2. INITIALIZE ENTITY ---
        HttpEntity<String> entity = new HttpEntity<>(requestJson, headers);
        // ------------------------------

        try {
            // Execute the POST request
            String finalUrl = API_URL + "?key=" + apiKey;
            
            // Use RestTemplate.exchange() to execute the request
            ResponseEntity<String> response = restTemplate.exchange(
                finalUrl, HttpMethod.POST, entity, String.class
            );

            String responseJson = response.getBody();
            
            // Parse the JSON response
            JsonNode root = objectMapper.readTree(responseJson);
            
            // Navigate the JSON path to extract the text
            String responseText = root.at("/candidates/0/content/parts/0/text").asText();
            
            // Check for potential block reason before returning
            if (responseText.isEmpty() && root.has("candidates")) {
                JsonNode candidate = root.at("/candidates/0");
                if (candidate.has("finishReason")) {
                    String reason = candidate.get("finishReason").asText();
                    if ("SAFETY".equals(reason) || "RECITATION".equals(reason)) {
                         return "I am sorry, your request was blocked due to a safety policy violation. Please rephrase your question.";
                    }
                }
            }
            
            return responseText;
        } catch (HttpClientErrorException e) {
            // This catches 4xx errors (e.g., API Key issues, Invalid Arguments)
            String errorBody = e.getResponseBodyAsString();
            System.err.println("Gemini 4XX Error: " + errorBody);
            // Throw a custom runtime exception that includes the service context
            throw new RuntimeException("Gemini Service Error: API returned client error: " + errorBody, e);
        } catch (ResourceAccessException e) {
            // This catches connection issues (e.g., network timeout, DNS failure)
            System.err.println("Connection Error: " + e.getMessage());
            throw new RuntimeException("Gemini Service Error: Failed to connect to Gemini API.", e);
        } catch (Exception e) {
            // This catches general JSON parsing errors, etc.
            System.err.println("General Parsing Crash: " + e.getMessage());
            throw new RuntimeException("Gemini Service Error: JSON Parsing Failed on Gemini Response: " + e.getMessage(), e);
        }
    }
}