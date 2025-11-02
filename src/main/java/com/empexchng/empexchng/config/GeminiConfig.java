package com.empexchng.empexchng.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "gemini")
public class GeminiConfig {

    private String apiKey;

    // Getter is essential for Thymeleaf/Controller to read the value
    public String getApiKey() {
        return apiKey;
    }

    // Setter is essential for Spring to inject the value
    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }
}
