package com.example.backend_ai_interview.controllers;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.backend_ai_interview.models.Media;
import com.example.backend_ai_interview.services.MediaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/media")
@CrossOrigin
@Tag(name = "Media", description = "API untuk upload dan transkripsi media")
public class MediaController {

    private static final Logger logger = LoggerFactory.getLogger(MediaController.class);

    @Autowired
    private MediaService mediaService;

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
        @RequestPart("file") MultipartFile file
    ) {
        try {
            Media media = mediaService.handleUpload(file);
            return ResponseEntity.ok(Map.of(
                "message", "Upload & transkripsi berhasil",
                "transcript", media.getTranscript()
            ));
        } catch (Exception e) {
            logger.error("Error during media upload and transcription", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", e.getMessage()));
        }
    }
}
