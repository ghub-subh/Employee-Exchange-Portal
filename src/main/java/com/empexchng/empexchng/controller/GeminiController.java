package com.empexchng.empexchng.controller;

// ... other imports
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.empexchng.empexchng.config.GeminiConfig; // Import the config class

@Controller
// Assuming you create a JobSeekerController
@RequestMapping("/jobseeker")
public class GeminiController { 

    @Autowired
    private GeminiConfig geminiConfig; // Inject the key holder

    @GetMapping("/gemini/panel")
    public String showJobSeekerPanel(Model model) {
        // --- PASS THE API KEY SECURELY TO THE MODEL ---
        model.addAttribute("geminiApiKey", geminiConfig.getApiKey()); 
        
        // ... add other necessary attributes (like loggedInUser) ...

        return "jobseeker/panel"; 
    }
    
    // ... other methods ...
}