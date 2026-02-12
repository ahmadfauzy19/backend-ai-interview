package com.example.backend_ai_interview.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
@JsonIgnoreProperties(ignoreUnknown = true)
public record QuestionSetDto(
    String level,
    String role,
    String technology,
    List<QuestionItemDto> questions
) {}