package com.example.backend_ai_interview.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.example.backend_ai_interview.models.QuestionSet;

import java.util.Optional;

public interface QuestionSetRepository
        extends MongoRepository<QuestionSet, String> {

    Optional<QuestionSet> findByLevel(String level);
}

