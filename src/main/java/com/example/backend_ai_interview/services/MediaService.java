package com.example.backend_ai_interview.services;
import java.nio.file.Files;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.backend_ai_interview.utils.Ffmpeg;

import java.io.File;
import java.nio.file.Paths;
import com.example.backend_ai_interview.models.Media;

@Service
public class MediaService {

  private static final String VIDEO_DIR = "storage/videos/";
  private static final String AUDIO_DIR = "storage/audio/";

  @Autowired
  private WhisperService whisperService;

  public Media handleUpload(MultipartFile file) throws Exception {

    String contentType = file.getContentType();
    if (contentType == null) {
      throw new IllegalArgumentException("Content type tidak terdeteksi");
    }

    Files.createDirectories(Paths.get(VIDEO_DIR));
    Files.createDirectories(Paths.get(AUDIO_DIR));

    String uuid = UUID.randomUUID().toString();
    String audioPath = AUDIO_DIR + uuid + ".wav";
    String videoPath = null;
    String transcript;

    /* ================= VIDEO ================= */
    if (contentType.startsWith("video/")) {

      videoPath = VIDEO_DIR + uuid + getExtension(file.getOriginalFilename());
      file.transferTo(new File(videoPath));

      // extract audio dari video
      Ffmpeg.extractAudio(videoPath, audioPath);

      transcript = whisperService.transcribe(new File(audioPath));

    }
    /* ================= AUDIO ================= */
    else if (contentType.startsWith("audio/")) {

      // langsung simpan audio
      file.transferTo(new File(audioPath));

      transcript = whisperService.transcribe(new File(audioPath));
    }
    /* ================= UNSUPPORTED ================= */
    else {
      throw new IllegalArgumentException("Format file tidak didukung");
    }

    Media media = new Media();
    // media.setVideoPath(videoPath);   // null jika audio
    // media.setAudioPath(audioPath);
    // media.setTranscript(transcript);
    // media.setLanguage("id");
    // media.setType(contentType.startsWith("video/") ? "video" : "audio");

    // return media;
    return new Media(
      videoPath,
      audioPath,
      transcript,
      "id",
      contentType.startsWith("video/") ? "video" : "audio"
    );
  }

  private String getExtension(String filename) {
    if (filename == null || !filename.contains(".")) {
      return ".webm";
    }
    return filename.substring(filename.lastIndexOf("."));
  }
}
