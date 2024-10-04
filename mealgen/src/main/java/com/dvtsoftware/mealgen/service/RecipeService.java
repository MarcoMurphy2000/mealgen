package com.dvtsoftware.mealgen.service;

import java.util.List;
import java.util.NoSuchElementException;

import com.dvtsoftware.mealgen.mapper.RecipeMapper;
import com.dvtsoftware.mealgen.model.domain.RecipeDomainObject;
import com.dvtsoftware.mealgen.model.entity.RecipeEntity;
import com.dvtsoftware.mealgen.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RecipeService {

    private static final String RECIPE_NOT_FOUND = "Recipe not found";

    private final RecipeRepository recipeRepository;
    private final RecipeMapper recipeMapper;

    @Autowired
    public RecipeService(RecipeRepository recipeRepository, RecipeMapper recipeMapper) {
        this.recipeRepository = recipeRepository;
        this.recipeMapper = recipeMapper;
    }
    
    public void createRecipe(RecipeDomainObject recipeDomainObject) {
        recipeRepository.save(recipeMapper.map_RecipeDO_to_RecipeEntity(recipeDomainObject));
    }

    public List<RecipeEntity> getAllRecipes() {
        return recipeRepository.findAll();
    }

    public RecipeEntity getRecipeById(Long id) {
        return recipeRepository.findById(id).orElseThrow(() -> new RuntimeException("RecipeEntity not found"));
    }

    public RecipeEntity updateRecipe(Long id, RecipeDomainObject recipeDomainObject) throws NoSuchElementException {
        checkRecipeExists(id);
        RecipeEntity recipeEntity = recipeMapper.map_RecipeDO_to_RecipeEntity(recipeDomainObject);
        recipeEntity.setId(id);
        return recipeRepository.save(recipeEntity);
    }

    public void deleteRecipe(Long id) {
        recipeRepository.deleteById(id);
    }

    public void checkRecipeExists(final Long id) throws NoSuchElementException {
        if (!recipeRepository.existsById(id)) {
            throw new NoSuchElementException(RECIPE_NOT_FOUND);
        }
    }
}
