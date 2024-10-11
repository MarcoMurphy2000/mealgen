package com.dvtsoftware.mealgen.service.impl;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import com.dvtsoftware.mealgen.mapper.RecipeMapper;
import com.dvtsoftware.mealgen.model.domain.RecipeDomainObject;
import com.dvtsoftware.mealgen.model.entity.RecipeEntity;
import com.dvtsoftware.mealgen.repository.RecipeRepository;
import com.dvtsoftware.mealgen.service.interfaces.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RecipeServiceImpl implements RecipeService {

    private static final String RECIPE_NOT_FOUND = "Recipe not found";

    private final RecipeRepository recipeRepository;
    private final RecipeMapper recipeMapper;

    @Autowired
    public RecipeServiceImpl(RecipeRepository recipeRepository, RecipeMapper recipeMapper) {
        this.recipeRepository = recipeRepository;
        this.recipeMapper = recipeMapper;
    }

    public List<RecipeDomainObject> getAllRecipes() {
        List<RecipeEntity> recipeEntities = recipeRepository.findAll();
        return recipeEntities.stream()
                .map(recipeMapper::mapRecipeEntityToRecipeDO)
                .toList();
    }

    public RecipeDomainObject getRecipeById(Long id) throws NoSuchElementException {
        Optional<RecipeEntity> recipeEntity = recipeRepository.findById(id);
        return recipeEntity.map(recipeMapper::mapRecipeEntityToRecipeDO)
                .orElseThrow(() -> new RuntimeException(RECIPE_NOT_FOUND));
    }

    public void updateRecipe(Long id, RecipeDomainObject recipeDomainObject) throws NoSuchElementException {
        checkRecipeExists(id);
        RecipeEntity recipeEntity = recipeMapper.mapRecipeDOToRecipeEntity(recipeDomainObject);
        recipeEntity.setId(id);
        recipeRepository.save(recipeEntity);
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
