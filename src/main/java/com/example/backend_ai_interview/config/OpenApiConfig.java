package com.example.backend_ai_interview.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("AI Interview Backend API")
                        .version("1.0.0")
                        .description("API untuk upload, extract audio, dan transkripsi media menggunakan Whisper AI")
                        .contact(new Contact()
                                .name("Padepokan 79")
                                .url("https://padepokan79.com")));
    }
}
