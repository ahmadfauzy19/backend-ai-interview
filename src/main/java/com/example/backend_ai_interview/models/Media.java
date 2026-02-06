package com.example.backend_ai_interview.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "media")
public class Media {
    @Id
    private String id;
    private String videoPath;
    private String audioPath;
    private String transcript;
    private String language;
    private long createdAt;
    private Integer score;
    
    public Media(String videoPath, String audioPath, String transcript, String language, Integer score) {
        this.videoPath = videoPath;
        this.audioPath = audioPath;
        this.transcript = transcript;
        this.language = language;
        this.score = score;
        this.createdAt = System.currentTimeMillis();
    }
}
