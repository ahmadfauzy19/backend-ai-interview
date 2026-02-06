package com.example.backend_ai_interview.models;

import java.util.List;
 
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Document(collection = "assessment_data")
public class AssessmentDocument {

    @Id
    private String id;

    private String level;

    private List<QuestionAnswer> listPertanyaan;
}

