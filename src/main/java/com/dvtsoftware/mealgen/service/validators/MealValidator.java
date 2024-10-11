package com.dvtsoftware.mealgen.service.validators;

import java.util.List;

import com.dvtsoftware.mealgen.model.domain.MealCategory;
import com.dvtsoftware.mealgen.model.entity.MealEntity;
import com.dvtsoftware.mealgen.model.entity.RecipeEntity;
import jakarta.validation.ValidationException;
import org.springframework.stereotype.Component;

@Component
public class MealValidator implements ModelValidator<MealEntity> {

    @Override
    public void validate(final MealEntity mealEntity) {
        try {
            verifyIngredients(mealEntity.getIngredients());
            verifyCalories(mealEntity.getCalories());
            verifyProtein(mealEntity.getProtein());
            verifyCarbohydrates(mealEntity.getCarbohydrates());
            verifyFat(mealEntity.getFat());
            verifyCategory(MealCategory.valueOf(mealEntity.getCategory()));
            verifyRecipe(mealEntity.getRecipe());
        } catch (Exception e) {
            throw new ValidationException(e.getMessage());
        }
    }

    public void verifyIngredients(final List<String> ingredients) {
        if (ingredients == null || ingredients.isEmpty()) {
            throw new ValidationException("Ingredients cannot be null or empty");
        }
    }

    public void verifyCalories(final double calories) {
        if (calories < 0) {
            throw new ValidationException("Calories cannot be negative");
        }
    }

    public void verifyProtein(final double protein) {
        if (protein < 0) {
            throw new ValidationException("Protein cannot be negative");
        }
    }

    public void verifyCarbohydrates(final double carbohydrates) {
        if (carbohydrates < 0) {
            throw new ValidationException("Carbohydrates cannot be negative");
        }
    }

    public void verifyFat(final double fat) {
        if (fat < 0) {
            throw new ValidationException("Fat cannot be negative");
        }
    }

    public void verifyCategory(final MealCategory category) {
        if (category == null) {
            throw new ValidationException("Category cannot be null.");
        }
    }

    public void verifyRecipe(final RecipeEntity recipe) {
        if (recipe == null) {
            throw new ValidationException("Recipe cannot be null");
        }
    }
}
