package com.example.backend_ai_interview.models;

import lombok.Data;
import java.util.List;
@Data
public class RubricItem {
    private String description;
    private List<String> keywords;
}
