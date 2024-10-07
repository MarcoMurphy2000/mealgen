package com.dvtsoftware.mealgen.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class OpenAIService {

    private static final String API_URL = "https://api.openai.com/v1/chat/completions";
    private static final String API_KEY = "sk-svcacct-ueXNZLLklFRJgiPZQeOwT3BlbkFJ0fGfYLTteJMeWI45GLWu";
    private static final String MODEL = "gpt-3.5-turbo";

    public String openAI(String prompt) throws IOException {
        return sendRequest(prompt);
    }

    private String sendRequest(String message) throws IOException {
        HttpURLConnection con = null;
        try {
            URL url = new URL(API_URL);
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Authorization", "Bearer " + API_KEY);
            con.setRequestProperty("Content-Type", "application/json");
            con.setDoOutput(true);

            // Escape newlines and special characters in the message
            String sanitizedMessage = message.replace("\n", "\\n").replace("\"", "\\\"");

            // Build the request body
            String body = "{\"model\": \"" + MODEL + "\", \"messages\": [{\"role\": \"user\", \"content\": \"" + sanitizedMessage + "\"}]}";

            // Log the constructed request body for debugging
            System.out.println("Request Body: " + body);

            // Write the request body
            try (OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream())) {
                writer.write(body);
                writer.flush();
            }

            // Read the response
            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
                    String inputLine;
                    StringBuilder response = new StringBuilder();
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }

                    // Log the raw response before parsing
                    System.out.println("Raw Response: " + response.toString());

                    return parseResponse(response.toString());
                }
            } else {
                // Log the error response for debugging
                try (BufferedReader errorReader = new BufferedReader(new InputStreamReader(con.getErrorStream()))) {
                    String errorLine;
                    StringBuilder errorResponse = new StringBuilder();
                    while ((errorLine = errorReader.readLine()) != null) {
                        errorResponse.append(errorLine);
                    }
                    System.err.println("Error Response from OpenAI: " + errorResponse.toString());
                }
                throw new IOException("HTTP request failed with response code " + responseCode);
            }
        } finally {
            if (con != null) {
                con.disconnect();
            }
        }
    }

    private String parseResponse(String response) {
        // Log the raw response for debugging
        System.out.println("Raw Response: " + response);

        try {
            // Attempt to parse the response as JSON
            JSONObject jsonResponse = new JSONObject(response);
            return jsonResponse.toString();  // Return the JSON-formatted response
        } catch (Exception e) {
            // If parsing fails, log the error and assume it's plain text
            System.err.println("Error parsing JSON response, treating as plain text: " + e.getMessage());
            return response;
        }
    }

}
