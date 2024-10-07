package com.dvtsoftware.mealgen.model.domain;

import lombok.Data;

@Data
public class Prompt {

    private String message;

    public Prompt(String message) {
        this.message = message;
    }
}
