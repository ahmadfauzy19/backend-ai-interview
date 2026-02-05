package com.example.backend_ai_interview.services;

import java.io.File;
import java.nio.file.Files;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.backend_ai_interview.models.Media;
import com.example.backend_ai_interview.utils.Ffmpeg;

@Service
public class MediaService {

    @Autowired
    private SttClient sttClient;

    public Media handleUpload(MultipartFile file) throws Exception {

        String contentType = file.getContentType();
        if (contentType == null) {
            throw new IllegalArgumentException("Content-Type tidak valid");
        }

        // ===== temp files =====
        File tempInput;
        File tempAudio;

        /* ================= VIDEO ================= */
        if (contentType.startsWith("video/")) {

            tempInput = Files.createTempFile("video-", getExtension(file.getOriginalFilename()))
                    .toFile();
            tempAudio = Files.createTempFile("audio-", ".wav").toFile();

            file.transferTo(tempInput);

            Ffmpeg.extractAudio(
                tempInput.getAbsolutePath(),
                tempAudio.getAbsolutePath()
            );

        }

        /* ================= AUDIO ================= */
        else if (contentType.startsWith("audio/")) {

            tempAudio = Files.createTempFile("audio-", ".wav").toFile();
            file.transferTo(tempAudio);
            tempInput = null;

        }

        else {
            throw new IllegalArgumentException(
                "Format tidak didukung: " + contentType
            );
        }

        // ===== STT =====
        String transcript = sttClient.transcribe(tempAudio);

        // ===== cleanup =====
        if (tempInput != null && tempInput.exists()) {
            tempInput.delete();
        }
        if (tempAudio.exists()) {
            tempAudio.delete();
        }

        Media media = new Media();
        media.setVideoPath(null);   // tidak disimpan
        media.setAudioPath(null);   // tidak disimpan
        media.setTranscript(transcript);
        media.setLanguage("id");

        return media;
    }

    private String getExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return ".webm";
        }
        return filename.substring(filename.lastIndexOf("."));
    }
}
