package com.example.backend_ai_interview.mapper;

import java.util.List;
import java.util.stream.Collectors;

import com.example.backend_ai_interview.dto.AnswerItemDto;
import com.example.backend_ai_interview.dto.InterviewSessionDto;
import com.example.backend_ai_interview.models.AnswerItem;
import com.example.backend_ai_interview.models.InterviewSession;

public final class InterviewSessionMapper {

    private InterviewSessionMapper() {
        // prevent instantiation
    }

    public static InterviewSessionDto toDto(InterviewSession entity) {
        if (entity == null) {
            return null;
        }

        List<AnswerItemDto> answerDtos = null;
        if (entity.getAnswers() != null) {
            answerDtos = entity.getAnswers().stream()
                .map(InterviewSessionMapper::toDto)
                .collect(Collectors.toList());
        }

        InterviewSessionDto dto = new InterviewSessionDto(
            entity.getId(),
            entity.getCandidateName(),
            entity.getLevel(),
            entity.getRole(),
            entity.getTechnology(),
            entity.getStartedAt(),
            entity.getEndedAt(),
            entity.getTotalScore(),
            answerDtos
        );

        return dto;
    }

    private static AnswerItemDto toDto(AnswerItem item) {
        return new AnswerItemDto(
            item.getQuestionId(),
            item.getQuestionText(),
            item.getAnswerText(),
            item.getScore(),
            item.getStart(),
            item.getEnd()
        );
    }
}