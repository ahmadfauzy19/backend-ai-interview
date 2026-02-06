package com.example.backend_ai_interview.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.backend_ai_interview.models.AssessmentDocument;

public interface AssessmentRepository 
        extends MongoRepository<AssessmentDocument, String> {

    Optional<AssessmentDocument> findByLevel(String level);
}
