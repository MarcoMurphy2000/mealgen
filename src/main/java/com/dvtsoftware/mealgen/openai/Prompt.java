package com.dvtsoftware.mealgen.openai;

import lombok.Data;

@Data
public class Prompt {

    private String message;

    public Prompt(String message) {
        this.message = message;
    }
}
