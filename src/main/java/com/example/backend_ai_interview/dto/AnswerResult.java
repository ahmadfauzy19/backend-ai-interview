package com.example.backend_ai_interview.dto;

import lombok.Data;

@Data
public class AnswerResult {
    private String questionId;
    private String pertanyaan;
    private Object rubrikPenilaian;
    private String jawabanKaryawan;
    private Integer score;
}

