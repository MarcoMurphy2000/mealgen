package com.dvtsoftware.mealgen.model.domain;

import lombok.Getter;

@Getter
public enum MealCategory {
    WEIGHT_LOSS("Weight Loss"),
    MUSCLE_GAIN("Muscle Gain"),
    MAINTENANCE("Maintenance");

    private final String displayName;

    MealCategory(String displayName) {
        this.displayName = displayName;
    }

}
