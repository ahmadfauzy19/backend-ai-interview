package com.example.backend_ai_interview.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AnswerItemDto {
    String questionId;
    String questionText;
    String answerText;
    Integer score;
    Double start;
    Double end;
}
