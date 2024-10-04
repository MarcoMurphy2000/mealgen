package com.dvtsoftware.mealgen.model.domain;

import java.util.List;

import lombok.Data;

@Data
public class RecipeDomainObject {

    private String recipeName;
    private List<String> steps;

    // Constructor with business logic for validation
    public RecipeDomainObject(String recipeName, List<String> steps) {
        this.recipeName = recipeName;
        this.steps = steps;
    }

    // Business logic to validate the recipe
    public boolean isValid() {
        return recipeName != null && !steps.isEmpty();
    }
}
