package com.example.backend_ai_interview.models;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;


@Document(collection = "question_sets")
@Data
public class QuestionSet {

    @JsonProperty("_id")
    @Id
    private String id;

    private String level;
    private String role;
    private String technology;
    private List<QuestionItem> questions;
}
