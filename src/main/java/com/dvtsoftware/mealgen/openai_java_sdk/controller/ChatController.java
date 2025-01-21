package com.dvtsoftware.mealgen.openai_java_sdk.controller;

import com.dvtsoftware.mealgen.openai_java_sdk.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService chatService;

    @Autowired
    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/message")
    public String sendMessage(@RequestBody String userMessage) {
        return chatService.sendMessage(userMessage);
    }

    @PostMapping("/reset")
    public String resetConversation() {
        chatService.resetConversation();
        return "Conversation history has been reset.";
    }
}
