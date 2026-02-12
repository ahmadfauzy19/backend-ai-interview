package com.example.backend_ai_interview.models;

import lombok.Data;
import java.util.Map;


@Data
public class QuestionItem {
    private String questionId;
    private String question;
    private Map<String, RubricItem> rubrics;
}

