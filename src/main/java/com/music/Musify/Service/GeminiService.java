package com.music.Musify.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.music.Musify.Entity.GeminiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Service
public class GeminiService {

    private final WebClient webClient;
    private final String apiKey = "AIzaSyCvnDtwV-s5ymnLwuO2XZtY5VsD3It90XM";

    public GeminiService() {
        this.webClient = WebClient.builder()
                .baseUrl("https://generativelanguage.googleapis.com")
                .build();
    }

    public GeminiResponse fetchSongDetails(String title) {
        String promptText = "Give me details about the song '" + title +
                "'. Include: artist, release date, short description, and 1 fun fact.";

        log.info("Generated Prompt: {}", promptText);

        String aiResponse = callGeminiApi(promptText);
        log.info("Raw Gemini API Response: {}", aiResponse);

        ObjectMapper mapper = new ObjectMapper();

        try {
            JsonNode root = mapper.readTree(aiResponse);
            String content = root
                    .path("candidates")
                    .get(0)
                    .path("content")
                    .path("parts")
                    .get(0)
                    .path("text")
                    .asText();

            log.info("Parsed Content: {}", content);

            String[] lines = content.split("\n");
            GeminiResponse response = new GeminiResponse();
            response.setArtist(lines.length > 0 ? lines[0].replace("Artist: ", "") : "Unknown Artist");
            response.setReleaseDate(lines.length > 1 ? lines[1].replace("Release Date: ", "") : "Unknown Release Date");
            response.setDescription(lines.length > 2 ? lines[2].replace("Description: ", "") : "No description available.");
            response.setFact(lines.length > 3 ? lines[3].replace("Fun fact: ", "") : "No fun fact available.");

            log.info("Final GeminiResponse: {}", response);
            return response;

        } catch (Exception e) {
            log.error("Error parsing Gemini response", e);

            GeminiResponse response = new GeminiResponse();
            response.setArtist("Unknown Artist");
            response.setReleaseDate("Unknown Release Date");
            response.setDescription("No description available.");
            response.setFact("No fun fact available.");
            return response;
        }
    }

    private String callGeminiApi(String promptText) {
        String requestBody = "{"
                + "\"contents\": ["
                + "  {"
                + "    \"parts\": [ { \"text\": \"" + promptText + "\" } ]"
                + "  }"
                + "]"
                + "}";

        log.info("Sending Request Body: {}", requestBody);

        String response = webClient.post()
                .uri("/v1beta/models/gemini-2.0-flash:generateContent?key=" + apiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        log.info("Received Response: {}", response);
        return response;
    }
}
