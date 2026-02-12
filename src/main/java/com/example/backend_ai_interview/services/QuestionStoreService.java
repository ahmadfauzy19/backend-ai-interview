package com.example.backend_ai_interview.services;

import java.util.Arrays;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import com.example.backend_ai_interview.dto.QuestionSetDto;
import com.example.backend_ai_interview.dto.QuestionItemDto;
import com.example.backend_ai_interview.models.QuestionSet;
import com.example.backend_ai_interview.mapper.QuestionSetMapper;

import com.example.backend_ai_interview.repository.QuestionSetRepository;

@Service
public class QuestionStoreService {

    private final QuestionSetRepository questionSetRepository;
    
    public QuestionStoreService(QuestionSetRepository questionSetRepository) {
        this.questionSetRepository = questionSetRepository;
    }

    public List<String> getAllLevels() {
        return Arrays.asList("JUNIOR", "MIDDLE", "SENIOR");
    }

    public QuestionSetDto loadContextByLevel(String level) {
        try {
            QuestionSet questionSet =  questionSetRepository.findByLevel(level.toUpperCase())
                .orElseThrow(() ->
                    new IllegalArgumentException(
                        "Interview context tidak ditemukan untuk level: " + level
                    )
                );
            return QuestionSetMapper.toDto(questionSet);
        } catch (DataAccessException e) {
            throw new RuntimeException(
                "Gagal mengakses database saat mengambil interview context",
                e
            );
        }
    }

    public QuestionItemDto getQuestionById(
            QuestionSetDto context,
            String questionId
    ) {
        try {
            return context.questions().stream()
                .filter(q -> q.questionId().equals(questionId))
                .findFirst()
                .orElseThrow(() ->
                    new IllegalArgumentException(
                        "Question ID tidak ditemukan: " + questionId
                    )
                );
        } catch (NullPointerException e) {
            throw new IllegalStateException(
                "Interview context belum terinisialisasi atau tidak valid",
                e
            );
        }
    }
}

