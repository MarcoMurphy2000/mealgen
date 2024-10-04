package com.dvtsoftware.mealgen.model.domain;

import lombok.Data;

@Data
public class MacronutrientCustomDomainObject {

    private double protein;
    private double carbohydrates;
    private double fat;

    public MacronutrientCustomDomainObject(double protein, double carbohydrates, double fat) {
        this.protein = protein;
        this.carbohydrates = carbohydrates;
        this.fat = fat;
    }

    // Business logic to check if macronutrient percentages are valid
    public boolean isValid() {
        double total = protein + carbohydrates + fat;
        return total == 100;
    }
}
