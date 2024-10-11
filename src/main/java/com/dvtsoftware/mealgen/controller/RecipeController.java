package com.dvtsoftware.mealgen.controller;

import java.util.List;
import java.util.NoSuchElementException;

import com.dvtsoftware.mealgen.generated.model.Recipe;
import com.dvtsoftware.mealgen.mapper.RecipeMapper;
import com.dvtsoftware.mealgen.model.domain.RecipeDomainObject;
import com.dvtsoftware.mealgen.service.impl.RecipeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/recipes")
public class RecipeController {

    private final RecipeServiceImpl recipeServiceImpl;
    private final RecipeMapper recipeMapper;

    @Autowired
    public RecipeController(RecipeServiceImpl recipeServiceImpl, RecipeMapper recipeMapper) {
        this.recipeServiceImpl = recipeServiceImpl;
        this.recipeMapper = recipeMapper;
    }

    @GetMapping
    public ResponseEntity<List<Recipe>> getAllRecipes() {
        List<Recipe> recipes = recipeServiceImpl.getAllRecipes().stream()
                .map(recipeMapper::mapRecipeDOToRecipeDTO)
                .toList();
        return ResponseEntity.ok(recipes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Recipe> getRecipeById(@PathVariable Long id) {
        Recipe recipe = recipeMapper.mapRecipeDOToRecipeDTO(recipeServiceImpl.getRecipeById(id));
        return ResponseEntity.ok(recipe);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Recipe> updateRecipe(@PathVariable Long id, @RequestBody Recipe recipe) {
        RecipeDomainObject recipeDomainObject = recipeMapper.mapRecipeDTOToRecipeDO(recipe);
        recipeServiceImpl.updateRecipe(id, recipeDomainObject);
        Recipe updatedRecipe = recipeMapper.mapRecipeDOToRecipeDTO(recipeDomainObject);
        return ResponseEntity.ok(updatedRecipe);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecipe(@PathVariable Long id) throws NoSuchElementException {
        recipeServiceImpl.deleteRecipe(id);
        return ResponseEntity.noContent().build();
    }
}
