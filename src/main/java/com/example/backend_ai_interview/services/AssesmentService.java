package com.example.backend_ai_interview.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.backend_ai_interview.models.AssessmentDocument;
import com.example.backend_ai_interview.models.QuestionAnswer;
import com.example.backend_ai_interview.repository.AssessmentRepository;
import com.example.backend_ai_interview.utils.PromptingPenilaian;

@Service
public class AssesmentService {

    private final AssessmentRepository repository;
    private final PromptingPenilaian promptBuilder;
    private final GeminiClient geminiClient;

    public AssesmentService(
            AssessmentRepository repository,
            PromptingPenilaian promptBuilder,
            GeminiClient geminiClient
    ) {
        this.repository = repository;
        this.promptBuilder = promptBuilder;
        this.geminiClient = geminiClient;
    }

    // ===============================
    // METHOD BARU (SINGLE QUESTION)
    // ===============================
    public Integer evaluateSingleAnswer(
            String level,
            String pertanyaan,
            Object rubrikPenilaian,
            String jawaban
    ) {

        String prompt = promptBuilder.buildPrompt(
                level,
                pertanyaan,
                rubrikPenilaian,
                jawaban
        );

        String score = geminiClient.evaluate(prompt);
        return Integer.valueOf(score);
    }

    // ===============================
    // METHOD LAMA (TETAP)
    // ===============================
    public List<Integer> evaluateByLevel(String level) {

        AssessmentDocument doc = repository.findByLevel(level)
                .orElseThrow(() -> new RuntimeException("Level tidak ditemukan"));

        List<Integer> scores = new ArrayList<>();

        for (QuestionAnswer qa : doc.getListPertanyaan()) {

            String prompt = promptBuilder.buildPrompt(
                    level,
                    qa.getPertanyaan(),
                    qa.getRubrikPenilaian(),
                    qa.getJawabanKaryawan()
            );

            String score = geminiClient.evaluate(prompt);
            scores.add(Integer.valueOf(score));
        }

        return scores;
    }
}
