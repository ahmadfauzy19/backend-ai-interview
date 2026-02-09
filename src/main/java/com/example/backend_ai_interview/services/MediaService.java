package com.example.backend_ai_interview.services;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.backend_ai_interview.dto.QuestionContext;
import com.example.backend_ai_interview.models.Media;
import com.example.backend_ai_interview.utils.Ffmpeg;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class MediaService {

    @Autowired
    private SttClient sttClient;

    @Autowired
    private AssesmentService assesmentService;

    public Media 
    handleUpload(MultipartFile file, String level, String questionId) throws Exception {

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

        // ===== get question & rubrik =====
        QuestionContext context = getQuestionContext(level, questionId);

        // ===== get score =====
        Integer score = assesmentService.evaluateSingleAnswer(
            level,
            context.pertanyaan(),
            context.rubrikPenilaian(),
            transcript
        );

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
        media.setScore(score);

        return media;
    }

    private QuestionContext getQuestionContext(String level, String questionId) {
        try {
            String fileName = switch (level.toUpperCase()) {
                case "JUNIOR" -> "junior.json";
                case "MIDDLE" -> "middle.json";
                case "SENIOR" -> "senior.json";
                default -> throw new IllegalArgumentException("Level tidak valid");
            };

            String path = "/com/example/backend_ai_interview/store/" + fileName;

            ObjectMapper mapper = new ObjectMapper();
            InputStream is = getClass().getResourceAsStream(path);

            Map<String, Object> root = mapper.readValue(is, Map.class);
            List<Map<String, Object>> list =
                    (List<Map<String, Object>>) root.get("list_pertanyaan");

            for (Map<String, Object> q : list) {
                if (questionId.equals(q.get("question_id"))) {
                    return new QuestionContext(
                            (String) q.get("pertanyaan"),
                            q.get("rubrik_penilaian")
                    );
                }
            }

            throw new RuntimeException("Question ID tidak ditemukan");

        } catch (IOException | RuntimeException e) {
            throw new RuntimeException("Gagal load pertanyaan & rubrik", e);
        }
    }


    private String getExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return ".webm";
        }
        return filename.substring(filename.lastIndexOf("."));
    }
}
