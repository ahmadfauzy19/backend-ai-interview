package com.example.backend_ai_interview.controllers;

import com.example.backend_ai_interview.services.MediaService;
import com.example.backend_ai_interview.models.Media;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.Map;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("/api/media")
@CrossOrigin
@Tag(name = "Media", description = "API untuk upload dan transkripsi media")
public class MediaController {
    @Autowired
    private MediaService mediaService;

    @PostMapping("/upload")
    @Operation(summary = "Upload dan transkripsi media", description = "Upload file video/audio dan dapatkan transkripsi dalam bahasa Indonesia")
    @ApiResponse(responseCode = "200", description = "Upload dan transkripsi berhasil")
    @ApiResponse(responseCode = "500", description = "Error pada server")
    public ResponseEntity<?> uploadMedia(@RequestParam("file") MultipartFile file) {
        try{
            Media media = mediaService.handleUpload(file);
                return ResponseEntity.ok(Map.of(
                    "message", "Upload & transkripsi berhasil",
                    "transcript", media.getTranscript()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
}
