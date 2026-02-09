package com.example.backend_ai_interview.services;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class QuestionStoreService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<String> getAllLevels() {
        return Arrays.asList("JUNIOR", "MIDDLE", "SENIOR");
    }

    public Map<String, Object> loadQuestionsByLevel(String level) {
        try {
            String fileName = switch (level.toUpperCase()) {
                case "JUNIOR" -> "junior.json";
                case "MIDDLE" -> "middle.json";
                case "SENIOR" -> "senior.json";
                default -> throw new IllegalArgumentException("Level tidak valid");
            };

            String path = "/com/example/backend_ai_interview/store/" + fileName;

            InputStream is = getClass().getResourceAsStream(path);
            return objectMapper.readValue(is, Map.class);

        } catch (IOException | IllegalArgumentException e) {
            throw new RuntimeException("Gagal membaca file soal", e);
        }
    }
}
