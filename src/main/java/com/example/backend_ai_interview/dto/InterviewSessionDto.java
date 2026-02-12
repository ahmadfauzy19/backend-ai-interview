package com.example.backend_ai_interview.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class InterviewSessionDto {
    String id;

    String candidateName;
    String level;
    String technology;
    String role;

    Long startedAt;
    Long endedAt;

    Integer totalScore;
    List<AnswerItemDto> answers;
}
