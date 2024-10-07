package com.dvtsoftware.mealgen.model.domain;

import java.util.List;

import lombok.Data;

@Data
public class MealRequestDomainObject {

    private List<String> ingredients;      // List of ingredients provided by the user
    private Boolean allowExtraIngredients; // Whether to allow extra ingredients not provided by the user
    private String mealCategory;           // Predefined meal category (e.g., "WeightLoss", "MuscleGain", "Maintenance")
    private String gender;                 // Gender of the user (e.g., "Male", "Female") - required if extra ingredients are allowed

    // Method to check if gender and meal category are provided when extra ingredients are allowed
    public boolean isAdditionalInfoProvided() {
        return allowExtraIngredients != null && allowExtraIngredients && gender != null && mealCategory != null;
    }
}
