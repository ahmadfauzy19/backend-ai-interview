package com.example.backend_ai_interview.services;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.backend_ai_interview.dto.InterviewContext;
import com.example.backend_ai_interview.dto.QuestionContext;

@Service
public class QuestionStoreService {

    private final ObjectMapper mapper = new ObjectMapper();

    public List<String> getAllLevels() {
        return Arrays.asList("JUNIOR", "MIDDLE", "SENIOR");
    }

    public InterviewContext loadContextByLevel(String level) {
        try {
            String fileName = switch (level.toUpperCase()) {
                case "JUNIOR" -> "junior.json";
                case "MIDDLE" -> "middle.json";
                case "SENIOR" -> "senior.json";
                default -> throw new IllegalArgumentException("Level tidak valid");
            };

            InputStream is = getClass()
                .getResourceAsStream("/com/example/backend_ai_interview/store/" + fileName);

            return mapper.readValue(is, InterviewContext.class);

        } catch (Exception e) {
            throw new RuntimeException("Gagal load interview context", e);
        }
    }

    public QuestionContext getQuestionById(InterviewContext context, String questionId) {
        return context.listPertanyaan().stream()
            .filter(q -> q.questionId().equals(questionId))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Question ID tidak ditemukan"));
    }
}

