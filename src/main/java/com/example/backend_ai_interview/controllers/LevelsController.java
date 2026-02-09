package com.example.backend_ai_interview.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend_ai_interview.services.QuestionStoreService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/levels")
@CrossOrigin
@Tag(name = "Levels", description = "API untuk mengambil daftar level interview")
public class LevelsController {

    @Autowired
    private QuestionStoreService questionStoreService;

    @GetMapping
    @Operation(summary = "Get all levels", description = "Mengambil semua level interview yang tersedia")
    @ApiResponse(responseCode = "200", description = "Daftar level berhasil diambil")
    public ResponseEntity<List<String>> getLevels() {
        List<String> levels = questionStoreService.getAllLevels();
        return ResponseEntity.ok(levels);
    }
}
