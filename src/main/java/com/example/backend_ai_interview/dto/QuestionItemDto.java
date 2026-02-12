package com.example.backend_ai_interview.dto;

import java.util.Map;

public record QuestionItemDto(
    String questionId,
    String question,
    Map<String, RubricItemDto> rubrics
) {}
