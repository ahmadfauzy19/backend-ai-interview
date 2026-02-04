package com.example.backend_ai_interview.services;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;
import com.example.backend_ai_interview.utils.Ffmpeg;
import com.example.backend_ai_interview.models.Media;

@Service
public class MediaService {

  private static final String VIDEO_DIR = "storage/videos/";
  private static final String AUDIO_DIR = "storage/audio/";

  @Autowired
  private WhisperService whisperService;

  public Media handleUpload(MultipartFile file) throws Exception {
    Files.createDirectories(Paths.get(VIDEO_DIR));
    Files.createDirectories(Paths.get(AUDIO_DIR));

    String uuid = UUID.randomUUID().toString();
    String videoPath = VIDEO_DIR + uuid + ".webm";
    String audioPath = AUDIO_DIR + uuid + ".wav";

    file.transferTo(new File(videoPath));

    Ffmpeg.extractAudio(videoPath, audioPath);

    String transcript = whisperService.transcribe(new File(audioPath));

    Media media = new Media();
    media.setVideoPath(videoPath);
    media.setAudioPath(audioPath);
    media.setTranscript(transcript);
    media.setLanguage("id"); 

    return media; // simpan ke DB jika pakai JPA
  }
}

