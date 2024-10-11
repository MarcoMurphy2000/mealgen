package com.dvtsoftware.mealgen.model.domain;

public enum MealCategory {
    WEIGHT_LOSS("Weight Loss"),
    MUSCLE_GAIN("Muscle Gain"),
    MAINTENANCE("Maintenance");

    private final String displayName;

    MealCategory(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
