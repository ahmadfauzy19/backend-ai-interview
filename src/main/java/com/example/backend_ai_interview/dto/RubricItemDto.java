package com.example.backend_ai_interview.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RubricItemDto {

    private final String description;
    private final List<String> keywords;
}
