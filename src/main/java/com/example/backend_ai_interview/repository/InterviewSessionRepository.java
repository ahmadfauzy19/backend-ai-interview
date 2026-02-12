package com.example.backend_ai_interview.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.example.backend_ai_interview.models.InterviewSession;

public interface InterviewSessionRepository
        extends MongoRepository<InterviewSession, String> {
}

