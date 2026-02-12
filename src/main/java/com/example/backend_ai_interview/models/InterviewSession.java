package com.example.backend_ai_interview.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Document("interview_sessions")
@Data
public class InterviewSession {

    @Id
    private String id;

    private String candidateName;
    private String level;
    private String role;
    private String technology;

    private Long startedAt;
    private Long endedAt;

    private Integer totalScore;
    private List<AnswerItem> answers;
}

