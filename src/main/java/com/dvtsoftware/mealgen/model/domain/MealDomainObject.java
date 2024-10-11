package com.dvtsoftware.mealgen.model.domain;

import java.util.List;

import lombok.Data;

@Data
public class MealDomainObject {

    private Long id;
    private List<String> ingredients;
    private double protein;
    private double carbohydrates;
    private double fat;
    private double calories;
    private String category; // e.g., weight loss, muscle gain, maintenance
    private RecipeDomainObject recipe;
}
