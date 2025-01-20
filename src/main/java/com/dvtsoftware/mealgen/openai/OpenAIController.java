package com.dvtsoftware.mealgen.openai;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/openai")
public class OpenAIController {

    private final OpenAIClient openAIClient;

    @Autowired
    public OpenAIController(OpenAIClient openAIClient) {
        this.openAIClient = openAIClient;
    }

    @PostMapping
    public ResponseEntity<String> chat(@RequestBody Prompt prompt) {
        try {
            String response = openAIClient.generateResponse(prompt.getMessage());
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error generating response: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid input: " + e.getMessage());
        }
    }
}
