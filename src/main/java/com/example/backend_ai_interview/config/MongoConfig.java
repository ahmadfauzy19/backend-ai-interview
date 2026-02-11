package com.example.backend_ai_interview.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

@Configuration
public class MongoConfig {

    @Value("${spring.data.mongodb.host:mongodb}")
    private String host;

    @Value("${spring.data.mongodb.port:27017}")
    private int port;

    @Value("${spring.data.mongodb.database:ai-interview}")
    private String database;

    @Bean
    @Primary
    public MongoClient mongoClient() {
        String connectionString = String.format("mongodb://%s:%d", host, port);
        System.out.println("=== CUSTOM MongoDB Connection String: " + connectionString + " ===");
        return MongoClients.create(connectionString);
    }

    @Bean
    @Primary
    public MongoTemplate mongoTemplate() {
        return new MongoTemplate(mongoClient(), database);
    }
}