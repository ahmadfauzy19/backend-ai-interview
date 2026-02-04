package com.example.backend_ai_interview.services;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.File;

@Service
public class WhisperService {

  @Value("${openai.api.key}")
  private String apiKey;

  private final OkHttpClient client = new OkHttpClient();

  public String transcribe(File audioFile) throws Exception {
    RequestBody body = new MultipartBody.Builder()
        .setType(MultipartBody.FORM)
        .addFormDataPart(
            "file",
            audioFile.getName(),
            RequestBody.create(audioFile, MediaType.parse("audio/wav"))
        )
        .addFormDataPart("model", "gpt-4o-transcribe")
        .build();

    Request request = new Request.Builder()
        .url("https://api.openai.com/v1/audio/transcriptions")
        .header("Authorization", "Bearer " + apiKey)
        .post(body)
        .build();

    try (Response response = client.newCall(request).execute()) {
      if (!response.isSuccessful()) {
        throw new RuntimeException("Whisper failed");
      }
      ObjectMapper mapper = new ObjectMapper();
      JsonNode json = mapper.readTree(response.body().string());
      return json.get("text").asText();
    }
  }
}

