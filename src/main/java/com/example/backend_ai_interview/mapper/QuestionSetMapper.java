package com.example.backend_ai_interview.mapper;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Map;

import com.example.backend_ai_interview.dto.QuestionItemDto;
import com.example.backend_ai_interview.dto.QuestionSetDto;
import com.example.backend_ai_interview.dto.RubricItemDto;
import com.example.backend_ai_interview.models.QuestionSet;
import com.example.backend_ai_interview.models.RubricItem;

public final class QuestionSetMapper {

    private QuestionSetMapper() {
        // prevent instantiation
    }

    public static QuestionSetDto toDto(QuestionSet entity) {
        if (entity == null) {
            return null;
        }

        List<QuestionItemDto> questions = entity.getQuestions().stream()
            .map(q -> new QuestionItemDto(
                q.getQuestionId(),
                q.getQuestion(),
                rubricItemMapper(q.getRubrics())
            ))
            .collect(Collectors.toList());

        return new QuestionSetDto(
            entity.getRole(),
            entity.getTechnology(),
            entity.getLevel(),
            questions
        );
    }

    public static Map<String, RubricItemDto> rubricItemMapper(
            Map<String, RubricItem> rubrics
    ) {
        if (rubrics == null) {
            return Map.of();
        }

        return rubrics.entrySet().stream()
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                e -> new RubricItemDto(
                    e.getValue().getDescription(),
                    e.getValue().getKeywords()
                )
            ));
    }
}
