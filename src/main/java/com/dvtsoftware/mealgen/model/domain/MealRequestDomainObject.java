package com.dvtsoftware.mealgen.model.domain;

import java.util.List;

import lombok.Data;

@Data
public class MealRequestDomainObject {

    private List<String> ingredients;
    private Boolean allowExtraIngredients;
    private MealCategory mealCategory;
    private String gender;
}
