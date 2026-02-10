package com.example.backend_ai_interview.services;

import java.io.File;
// import java.io.IOException;
// import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
// import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.backend_ai_interview.dto.AnswerResult;
import com.example.backend_ai_interview.dto.QuestionContext;
import com.example.backend_ai_interview.dto.Segment;
import com.example.backend_ai_interview.dto.Segments;
import com.example.backend_ai_interview.models.Media;
import com.example.backend_ai_interview.utils.Ffmpeg;
// import com.fasterxml.jackson.databind.ObjectMapper;
// import com.example.backend_ai_interview.services.AssesmentService;
// import com.example.backend_ai_interview.services.SttClient;
// import com.example.backend_ai_interview.services.QuestionStoreService;
import com.example.backend_ai_interview.dto.InterviewContext;

@Service
public class MediaService {

    @Autowired
    private SttClient sttClient;

    @Autowired
    private AssesmentService assesmentService;

    @Autowired
    private QuestionStoreService questionStoreService;

    public Media handleUpload(
            MultipartFile file,
            String level,
            Segments segments
    ) throws Exception {

        // ===== LOAD CONTEXT LEVEL =====
        InterviewContext context = questionStoreService.loadContextByLevel(level);
        // context punya: role, technology, list_pertanyaan

        // ===== simpan temp video =====
        File tempVideo = Files.createTempFile("video-", ".webm").toFile();
        file.transferTo(tempVideo);

        // ===== extract audio full =====
        File fullAudio = Files.createTempFile("audio-full-", ".wav").toFile();
        Ffmpeg.extractAudio(tempVideo.getAbsolutePath(), fullAudio.getAbsolutePath());

        List<AnswerResult> answers = new ArrayList<>();

        for (Segment seg : segments.items()) {

            // ===== potong audio =====
            File cutAudio = Files.createTempFile(
                "audio-" + seg.questionId() + "-", ".wav"
            ).toFile();

            Ffmpeg.cutAudio(
                fullAudio.getAbsolutePath(),
                cutAudio.getAbsolutePath(),
                seg.start(),
                seg.end()
            );

            // ===== STT =====
            String jawaban = sttClient.transcribe(cutAudio);

            // ===== ambil pertanyaan + rubrik =====
            QuestionContext qc =
                questionStoreService.getQuestionById(context, seg.questionId());

            // ===== SCORING PER PERTANYAAN =====
            Integer score = assesmentService.evaluateSingleAnswer(
                level,
                qc.pertanyaan(),
                qc.rubrikPenilaian(),
                jawaban
            );

            // ===== SIMPAN KE CONTEXT =====
            AnswerResult ar = new AnswerResult();
            ar.setQuestionId(seg.questionId());
            ar.setPertanyaan(qc.pertanyaan());
            ar.setRubrikPenilaian(qc.rubrikPenilaian());
            ar.setJawabanKaryawan(jawaban);
            ar.setScore(score);

            answers.add(ar);

            cutAudio.delete();
        }

        // ===== HITUNG SCORE FINAL =====
        int finalScore = Math.round(
            (float) answers.stream()
                .mapToInt(AnswerResult::getScore)
                .average()
                .orElse(0)
        );

        // ===== BUILD MEDIA =====
        Media media = new Media();
        media.setLevel(context.level());
        media.setRole(context.role());
        media.setTechnology(context.technology());
        media.setAnswers(answers);
        media.setFinalScore(finalScore);

        tempVideo.delete();
        fullAudio.delete();

        return media;
    }
}
