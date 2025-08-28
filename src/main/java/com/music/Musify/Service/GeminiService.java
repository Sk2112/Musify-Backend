package com.music.Musify.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.music.Musify.Entity.GeminiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Service
public class GeminiService {

    private final WebClient webClient;

    @Value("${gemini.api.key}")
    private String apiKey;

    private final ObjectMapper mapper = new ObjectMapper();

    public GeminiService() {
        this.webClient = WebClient.builder()
                .baseUrl("https://generativelanguage.googleapis.com")
                .build();
    }

    public GeminiResponse fetchSongDetails(String title) {
        String promptText = String.format(
                "Provide details about the song '%s'. " +
                        "Respond ONLY with valid JSON (no explanation, no extra text): " +
                        "{ \"artist\": \"\", \"releaseDate\": \"\", \"description\": \"\", \"fact\": \"\" }",
                title
        );

        log.info("Generated Prompt: {}", promptText);

        String aiResponse = callGeminiApi(promptText);
        log.info("Raw Gemini API Response: {}", aiResponse);

        try {
            JsonNode root = mapper.readTree(aiResponse);

            JsonNode candidates = root.path("candidates");
            if (!candidates.isArray() || candidates.isEmpty()) {
                throw new RuntimeException("No candidates found in Gemini response");
            }

            JsonNode content = candidates.get(0)
                    .path("content")
                    .path("parts");

            if (!content.isArray() || content.isEmpty()) {
                throw new RuntimeException("No content parts found in Gemini response");
            }

            String textContent = content.get(0).path("text").asText();
            log.info("Parsed Content (raw): {}", textContent);

            // 🔥 Fix: remove ```json ... ``` wrappers if present
            String cleanJson = textContent
                    .replaceAll("```json", "")
                    .replaceAll("```", "")
                    .trim();

            log.info("Cleaned JSON for parsing: {}", cleanJson);

            // Parse into JSON object
            JsonNode responseJson = mapper.readTree(cleanJson);

            GeminiResponse response = new GeminiResponse();
            response.setArtist(responseJson.path("artist").asText("Unknown Artist"));
            response.setReleaseDate(responseJson.path("releaseDate").asText("Unknown Release Date"));
            response.setDescription(responseJson.path("description").asText("No description available."));
            response.setFact(responseJson.path("fact").asText("No fun fact available."));

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
                + "    \"parts\": [ { \"text\": \"" + promptText.replace("\"", "\\\"") + "\" } ]"
                + "  }"
                + "]"
                + "}";

        log.info("Sending Request Body: {}", requestBody);

        return webClient.post()
                .uri("/v1beta/models/gemini-2.0-flash:generateContent?key=" + apiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
