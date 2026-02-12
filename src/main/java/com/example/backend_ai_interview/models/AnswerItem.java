package com.example.backend_ai_interview.models;

import lombok.Data;
@Data
public class AnswerItem {
    private String questionId;
    private String questionText;
    private String answerText;
    private Integer score;
    private Double start;
    private Double end;
}

