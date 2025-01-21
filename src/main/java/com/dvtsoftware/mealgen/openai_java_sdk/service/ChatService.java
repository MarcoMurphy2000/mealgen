package com.dvtsoftware.mealgen.openai_java_sdk.service;

import java.util.ArrayList;
import java.util.List;

import com.openai.client.OpenAIClient;
import com.openai.models.ChatCompletion;
import com.openai.models.ChatCompletionCreateParams;
import com.openai.models.ChatCompletionUserMessageParam;
import com.openai.models.ChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatService {

    private final OpenAIClient openAIChatClient; // Injected from OpenAIConfig
    private final List<ChatCompletionUserMessageParam> conversationHistory;

    @Autowired
    // Constructor injection of the OpenAIClient
    public ChatService(OpenAIClient openAIChatClient) {
        this.openAIChatClient = openAIChatClient;
        this.conversationHistory = new ArrayList<>();

        // Initialize the conversation with a system message
        this.conversationHistory.add(
                ChatCompletionUserMessageParam.builder()
                        .role(ChatCompletionUserMessageParam.Role.of("system"))
                        .content("You are a helpful assistant.")
                        .build()
        );
    }

    /**
     * Sends a user message to OpenAI and retrieves the assistant's response.
     *
     * @param userMessage The user's input message.
     * @return The assistant's response.
     */
    public String sendMessage(String userMessage) {
        // Add the user's message to the conversation history
        this.conversationHistory.add(
                ChatCompletionUserMessageParam.builder()
                        .role(ChatCompletionUserMessageParam.Role.of("user"))
                        .content(userMessage)
                        .build()
        );

        // Build the OpenAI request
        ChatCompletionCreateParams.Builder paramsBuilder = ChatCompletionCreateParams.builder()
                .model(ChatModel.GPT_4); // Specify GPT-4 or GPT-3.5

        // Add all messages from the conversation history
        for (ChatCompletionUserMessageParam message : conversationHistory) {
            paramsBuilder.addMessage(message);
        }

        ChatCompletionCreateParams params = paramsBuilder.build();

        // Call OpenAI API using the injected openAIChatClient
        ChatCompletion response = openAIChatClient.chat().completions().create(params);

        // Extract the assistant's response
        String assistantResponse = response.choices().get(0).message().content().orElse("");

        // Add the assistant's response to the conversation history
        this.conversationHistory.add(
                ChatCompletionUserMessageParam.builder()
                        .role(ChatCompletionUserMessageParam.Role.of("assistant"))
                        .content(assistantResponse)
                        .build()
        );

        return assistantResponse;
    }

    /**
     * Resets the conversation history to its initial state.
     */
    public void resetConversation() {
        this.conversationHistory.clear();
        this.conversationHistory.add(
                ChatCompletionUserMessageParam.builder()
                        .role(ChatCompletionUserMessageParam.Role.of("system"))
                        .content("You are a helpful assistant.")
                        .build()
        );
    }
}
