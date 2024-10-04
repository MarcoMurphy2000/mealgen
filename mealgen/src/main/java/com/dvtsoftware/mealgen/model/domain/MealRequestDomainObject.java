package com.dvtsoftware.mealgen.model.domain;

import java.util.List;

import lombok.Data;

@Data
public class MealRequestDomainObject {

    private List<String> ingredients;
    private boolean allowExtraIngredients;
    private MacronutrientCustomDomainObject macronutrientCustomDomainObject;
    private String mealCategory;

    public MealRequestDomainObject(List<String> ingredients, boolean allowExtraIngredients, MacronutrientCustomDomainObject macronutrientCustomDomainObject, String mealCategory) {
        this.ingredients = ingredients;
        this.allowExtraIngredients = allowExtraIngredients;
        this.macronutrientCustomDomainObject = macronutrientCustomDomainObject;
        this.mealCategory = mealCategory;
    }
}
