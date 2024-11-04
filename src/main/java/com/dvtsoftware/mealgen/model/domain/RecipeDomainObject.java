package com.dvtsoftware.mealgen.model.domain;

import java.util.List;

import lombok.Data;

@Data
public class RecipeDomainObject {

    private Long id;
    private List<String> steps; // Instructions for preparing the meal
}
