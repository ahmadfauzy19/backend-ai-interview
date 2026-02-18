package com.example.backend_ai_interview.services;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.backend_ai_interview.dto.InterviewSessionDto;
import com.example.backend_ai_interview.dto.QuestionItemDto;
import com.example.backend_ai_interview.dto.Segment;
import com.example.backend_ai_interview.dto.Segments;
import com.example.backend_ai_interview.models.AnswerItem;
import com.example.backend_ai_interview.models.InterviewSession;
import com.example.backend_ai_interview.repository.InterviewSessionRepository;
import com.example.backend_ai_interview.utils.Ffmpeg;
import com.example.backend_ai_interview.dto.QuestionSetDto;
import com.example.backend_ai_interview.mapper.InterviewSessionMapper;

@Service
public class InterviewService {

    @Autowired
    private SttClient sttClient;

    @Autowired
    private AssesmentService assesmentService;

    @Autowired
    private QuestionStoreService questionStoreService;

    private final InterviewSessionRepository interviewSessionRepository;

    public InterviewService(InterviewSessionRepository interviewSessionRepository) {
        this.interviewSessionRepository = interviewSessionRepository;
    }

    public InterviewSessionDto handleUpload(
            String CandidateName,
            MultipartFile file,
            String level,
            Segments segments
    ) throws Exception {

        // ===== LOAD CONTEXT LEVEL =====
        QuestionSetDto context = questionStoreService.loadContextByLevel(level);
        
        // ===== simpan temp video =====
        File tempVideo = Files.createTempFile("video-", ".webm").toFile();
        file.transferTo(tempVideo);

        // ===== extract audio full =====
        File fullAudio = Files.createTempFile("audio-full-", ".wav").toFile();
        Ffmpeg.extractAudio(tempVideo.getAbsolutePath(), fullAudio.getAbsolutePath());

        List<AnswerItem> answers = new ArrayList<>();

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
            QuestionItemDto qc =
                questionStoreService.getQuestionById(context, seg.questionId());

            // ===== SCORING PER PERTANYAAN =====
            Integer score = assesmentService.evaluateSingleAnswer(
                level,
                qc.question(),
                qc.rubrics(),
                jawaban
            );

            // ===== SIMPAN KE CONTEXT =====
            AnswerItem ar = new AnswerItem();
            ar.setQuestionId(seg.questionId());
            ar.setQuestionText(qc.question());
            ar.setAnswerText(jawaban);
            ar.setScore(score);
            ar.setStart(seg.start());
            ar.setEnd(seg.end());

            answers.add(ar);

            cutAudio.delete();
        }

        // ===== HITUNG SCORE FINAL =====
        int finalScore = Math.round(
            (float) answers.stream()
                .mapToInt(AnswerItem::getScore)
                .average()
                .orElse(0)
        );

        // ===== BUILD INTEVIEW SESSION =====
        InterviewSession interviewSession = new InterviewSession();
        interviewSession.setCandidateName(CandidateName);
        interviewSession.setLevel(context.level());
        interviewSession.setRole(context.role());
        interviewSession.setTechnology(context.technology());
        interviewSession.setAnswers(answers);
        interviewSession.setTotalScore(finalScore);
        // ===== SAVE TO DATABASE =====
        InterviewSession savedSession = interviewSessionRepository.save(interviewSession);

        tempVideo.delete();
        fullAudio.delete();

        return InterviewSessionMapper.toDto(savedSession);
    }
}
