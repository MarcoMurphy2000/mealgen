package com.dvtsoftware.mealgen.service.validators;

import java.util.List;

import com.dvtsoftware.mealgen.model.entity.RecipeEntity;
import jakarta.validation.ValidationException;
import org.springframework.stereotype.Component;

@Component
public class RecipeValidator implements ModelValidator<RecipeEntity> {

    @Override
    public void validate(final RecipeEntity recipeEntity) {
        try {
            verifyRecipeName(recipeEntity.getRecipeName());
            verifySteps(recipeEntity.getSteps());
        } catch (Exception e) {
            throw new ValidationException(e.getMessage());
        }
    }

    public void verifyRecipeName(final String recipeName) {
        if (recipeName == null || recipeName.trim().isEmpty()) {
            throw new ValidationException("Recipe name cannot be null or empty");
        }
    }

    public void verifySteps(final List<String> steps) {
        if (steps == null || steps.isEmpty()) {
            throw new ValidationException("Recipe steps cannot be null or empty");
        }

        for (String step : steps) {
            if (step == null || step.trim().isEmpty()) {
                throw new ValidationException("Recipe steps cannot contain null or empty values");
            }
        }
    }
}
