package com.dvtsoftware.mealgen.service.interfaces;

import java.util.List;
import java.util.NoSuchElementException;

import com.dvtsoftware.mealgen.model.domain.RecipeDomainObject;

public interface RecipeService {

    List<RecipeDomainObject> getAllRecipes();

    RecipeDomainObject getRecipeById(Long id) throws NoSuchElementException;

    void updateRecipe(Long id, RecipeDomainObject recipeDomainObject) throws NoSuchElementException;

    void deleteRecipe(Long id);

    void checkRecipeExists(Long id) throws NoSuchElementException;
}
