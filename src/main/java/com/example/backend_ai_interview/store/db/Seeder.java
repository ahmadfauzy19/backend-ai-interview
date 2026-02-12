package com.example.backend_ai_interview.store.db;

import com.example.backend_ai_interview.models.QuestionSet;
import com.example.backend_ai_interview.repository.QuestionSetRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@Component
@RequiredArgsConstructor
public class Seeder implements CommandLineRunner {

    private final QuestionSetRepository repository;
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void run(String... args) throws Exception {
        System.out.println("🔥 Seeder is running");

        // Cegah double insert
        if (repository.count() > 0) {
            System.out.println("📦 QuestionSet already exists, skip seeding");
            return;
        }

        load("junior_.json");
        load("middle_.json");
        load("senior_.json");
    }

    private void load(String fileName) throws Exception {

        InputStream is = getClass().getResourceAsStream(
            "/com/example/backend_ai_interview/store/" + fileName
        );

        if (is == null) {
            throw new RuntimeException("File not found: " + fileName);
        }

        QuestionSet qs = mapper.readValue(is, QuestionSet.class);
        repository.save(qs);

        System.out.println("✅ Loaded question set: " + qs.getLevel());
    }
}

