package com.example.backend_ai_interview.models;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.example.backend_ai_interview.dto.AnswerResult;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Document(collection = "media")
public class Media {

    @Id
    private String id;

    private String level;
    private String role;
    private String technology;

    // hasil lengkap interview
    private List<AnswerResult> answers;

    // nilai akhir keseluruhan
    private Integer finalScore;

    private long createdAt = System.currentTimeMillis();
}
