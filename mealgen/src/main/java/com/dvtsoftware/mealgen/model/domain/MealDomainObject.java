package com.dvtsoftware.mealgen.model.domain;

import java.util.List;

import lombok.Data;

@Data
public class MealDomainObject {

    private List<String> ingredients;
    private double protein;
    private double carbohydrates;
    private double fat;
    private double calories;
    private String category;

    // Business logic for calculating total calories
    public MealDomainObject(List<String> ingredients, double protein, double carbohydrates, double fat, String category) {
        this.ingredients = ingredients;
        this.protein = protein;
        this.carbohydrates = carbohydrates;
        this.fat = fat;
        this.calories = calculateCalories(protein, carbohydrates, fat);
        this.category = category;
    }

    private double calculateCalories(double protein, double carbohydrates, double fat) {
        return (protein * 4) + (carbohydrates * 4) + (fat * 9);
    }

    public boolean isValid() {
        return !ingredients.isEmpty() && calories > 0;
    }
}
