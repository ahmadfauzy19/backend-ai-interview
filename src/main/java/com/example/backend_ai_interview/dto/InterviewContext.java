package com.example.backend_ai_interview.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
@JsonIgnoreProperties(ignoreUnknown = true)
public record InterviewContext(
    String level,
    String role,
    String technology,

    @JsonProperty("list_pertanyaan")
    List<QuestionContext> listPertanyaan
) {}