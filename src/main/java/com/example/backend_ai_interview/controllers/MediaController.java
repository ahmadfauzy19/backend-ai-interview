package com.example.backend_ai_interview.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.backend_ai_interview.models.Media;
import com.example.backend_ai_interview.services.MediaService;
import com.example.backend_ai_interview.services.QuestionStoreService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.example.backend_ai_interview.dto.InterviewContext;
import com.example.backend_ai_interview.dto.Segments;

@RestController
@RequestMapping("/api/media")
@CrossOrigin
@Tag(name = "Media", description = "API untuk upload dan transkripsi media")
public class MediaController {

    @Autowired
    private MediaService mediaService;
    @Autowired
    private QuestionStoreService questionStoreService;

    @PostMapping(
        value = "/upload",
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    @Operation(
        summary = "Upload dan transkripsi media",
        description = "Upload file video/audio dan dapatkan transkripsi dalam bahasa Indonesia"
    )
    @ApiResponse(responseCode = "200", description = "Upload dan transkripsi berhasil")
    @ApiResponse(responseCode = "500", description = "Error pada server")
    public ResponseEntity<?> uploadMedia(
        @Parameter(description = "File video atau audio", required = true)
        @RequestPart("file") MultipartFile file,
        @RequestPart("level") String level,
        @RequestPart("segments") Segments segments
    ) throws Exception {
        Media media = mediaService.handleUpload(file, level, segments);
        return ResponseEntity.ok(Map.of(
            "message", "Upload & transkripsi berhasil",
            "level", level,
            "segments", segments,
            "listAnswers", media.getAnswers(),
            "score", media.getFinalScore()
        ));
    }

    @GetMapping("/{level}")
    public ResponseEntity<?> getQuestionsByLevel(@PathVariable String level) {
       InterviewContext data = questionStoreService.loadContextByLevel(level);
        return ResponseEntity.ok(data);
    }


}
