package com.dvtsoftware.mealgen.openai;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

@Service
public class OpenAIService {

    private static final String API_URL = "https://api.openai.com/v1/chat/completions";
    private static final String API_KEY = "sk-proj-3x7AejrN0UuYsvP7XoWpT3BlbkFJ7DrGoQdrCDnQ9N6sjiLR";  // Ensure you replace with your correct API key
    private static final String MODEL = "gpt-3.5-turbo";

    private final ObjectMapper objectMapper = new ObjectMapper();

    public String generateResponse(String prompt) throws IOException {
        HttpURLConnection con = null;
        try {
            URL url = new URL(API_URL);
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Authorization", "Bearer " + API_KEY);
            con.setRequestProperty("Content-Type", "application/json");
            con.setDoOutput(true);

            // Build the request payload as a Map
            Map<String, Object> payload = new HashMap<>();
            payload.put("model", MODEL);
            payload.put("messages", List.of(Map.of("role", "user", "content", prompt)));

            // Serialize the payload to JSON
            String jsonPayload = objectMapper.writeValueAsString(payload);

            // Write the request body
            try (OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream())) {
                writer.write(jsonPayload);
                writer.flush();
            }

            // Check the response code
            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
                    String inputLine;
                    StringBuilder response = new StringBuilder();
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    return response.toString();
                }
            } else {
                // Log error details for debugging
                System.err.println("HTTP request failed with response code " + responseCode);

                // Capture and log the error message from OpenAI
                try (BufferedReader errorReader = new BufferedReader(new InputStreamReader(con.getErrorStream()))) {
                    String errorLine;
                    StringBuilder errorResponse = new StringBuilder();
                    while ((errorLine = errorReader.readLine()) != null) {
                        errorResponse.append(errorLine);
                    }
                    System.err.println("Error response from OpenAI: " + errorResponse.toString());
                }

                throw new IOException("HTTP request failed with response code " + responseCode);
            }
        } finally {
            if (con != null) {
                con.disconnect();
            }
        }
    }
}
