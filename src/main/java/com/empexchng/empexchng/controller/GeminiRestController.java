package com.empexchng.empexchng.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.empexchng.empexchng.service.GeminiService;

@RestController
@RequestMapping("/api/gemini")
public class GeminiRestController {

    @Autowired
    private GeminiService geminiService;

    // Inner classes for Request/Response DTOs
    public static class ChatRequest {
        private String message;
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }

    public static class ChatResponse {
        private String responseText;
        public ChatResponse(String responseText) { this.responseText = responseText; }
        public String getResponseText() { return responseText; }
        public void setResponseText(String responseText) { this.responseText = responseText; }
    }

    @PostMapping("/chat")
    public ResponseEntity<ChatResponse> chat(@RequestBody ChatRequest request) {
        if (request.getMessage() == null || request.getMessage().isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        try {
            String responseText = geminiService.generateJobResponse(request.getMessage());
            return ResponseEntity.ok(new ChatResponse(responseText));
        } catch (Exception e) {
            System.err.println("Gemini Service Error: " + e.getMessage());
            return ResponseEntity.status(500)
                .body(new ChatResponse("I'm sorry, I encountered an error. Please try again later."));
        }
    }
}
