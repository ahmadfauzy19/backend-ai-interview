package com.example.backend_ai_interview.models;

import lombok.Data;
import lombok.NoArgsConstructor;  
 
@Data
@NoArgsConstructor
public class QuestionAnswer {

    private String pertanyaan;
    private String rubrikPenilaian;
    private String jawabanKaryawan;
}

