package com.example.backend_ai_interview.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.backend_ai_interview.config.GeminiPropertiesConfig;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;

@Service
public class GeminiClient {

    private final Client client;
    private final String model;

    @Autowired
    public GeminiClient(GeminiPropertiesConfig props) {
        // Google GenAI library requires GOOGLE_API_KEY environment variable
        String apiKey = props.getApiKey();
        if (apiKey != null && !apiKey.isBlank()) {
            // Set as GOOGLE_API_KEY for the Google GenAI library
            System.setProperty("GOOGLE_API_KEY", apiKey);
        } else {
            // Tidak ada API key, lanjutkan tanpa meng-set system property
            System.err.println("Warning: GEMINI_API_KEY not set; GeminiClient will be limited.");
        }

        this.model = props.getModel();

        if (apiKey != null && !apiKey.isBlank()) {
            this.client = new Client();
        } else {
            this.client = null; // Gemini disabled
        }
    }


    /**
     * Kirim prompt ke Gemini dan ambil text response
     */
    public String evaluate(String prompt) {
        if (this.client == null) {
            System.err.println("GeminiClient not available; returning fallback score '0'.");
            return "0";
        }

        System.out.println("client model: " + client);

        GenerateContentResponse response =
                client.models.generateContent(
                        model,
                        prompt,
                        null
                );

        return response.text();
    }
}
