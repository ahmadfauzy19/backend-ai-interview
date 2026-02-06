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
        // API Key diambil otomatis dari ENV: GEMINI_API_KEY
        // SET KE ENV
        System.setProperty("GEMINI_API_KEY", props.getApiKey());

        this.model = props.getModel();

        this.client = new Client();
    }


    /**
     * Kirim prompt ke Gemini dan ambil text response
     */
    public String evaluate(String prompt) {
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
