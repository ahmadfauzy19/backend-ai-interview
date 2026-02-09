package com.example.backend_ai_interview.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;
import com.example.backend_ai_interview.dto.RubrikContext;

public record QuestionContext(

    @JsonProperty("question_id")
    String questionId,
    String pertanyaan,

    @JsonProperty("rubrik_penilaian")
    Map<String, String> rubrikPenilaian,

    @JsonProperty("jawaban_karyawan")
    String jawabanKaryawan,

    Integer score
) {}
