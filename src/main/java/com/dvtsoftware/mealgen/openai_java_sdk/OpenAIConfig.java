package com.dvtsoftware.mealgen.openai_java_sdk;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAIConfig {

    @Bean
    public OpenAIClient openAIChatClient() {
        return OpenAIOkHttpClient.builder()
                .apiKey("sk-proj-3x7AejrN0UuYsvP7XoWpT3BlbkFJ7DrGoQdrCDnQ9N6sjiLR") // Replace with your actual API key
                .build();
    }
}
