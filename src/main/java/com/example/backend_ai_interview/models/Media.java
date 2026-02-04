package com.example.backend_ai_interview.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

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
    private String type;
    
    public Media(String videoPath, String audioPath, String transcript, String language, String type) {
        this.videoPath = videoPath;
        this.audioPath = audioPath;
        this.transcript = transcript;
        this.language = language;
        this.type = type;
        this.createdAt = System.currentTimeMillis();
    }
}
