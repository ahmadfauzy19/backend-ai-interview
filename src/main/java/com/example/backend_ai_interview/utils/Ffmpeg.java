package com.example.backend_ai_interview.utils;

public class Ffmpeg {
    public static void extractAudio(String videoFilePath, String audioFilePath) throws Exception {
        ProcessBuilder pb = new ProcessBuilder(
            "ffmpeg","-y", "-i", videoFilePath, "-q:a", "0", "-map", "a", audioFilePath  
        );
        pb.inheritIO();
        Process process = pb.start();
        try {
            process.waitFor();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
