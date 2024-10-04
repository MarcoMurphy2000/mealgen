package com.dvtsoftware.mealgen.controller;

import java.util.List;
import java.util.NoSuchElementException;

import com.dvtsoftware.mealgen.generated.model.Recipe;
import com.dvtsoftware.mealgen.mapper.RecipeMapper;
import com.dvtsoftware.mealgen.model.domain.RecipeDomainObject;
import com.dvtsoftware.mealgen.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/recipes")
public class RecipeController {

    private final RecipeService recipeService;
    private final RecipeMapper recipeMapper;

    @Autowired
    public RecipeController(RecipeService recipeService, RecipeMapper recipeMapper) {
        this.recipeService = recipeService;
        this.recipeMapper = recipeMapper;
    }

    @PostMapping
    public ResponseEntity<Recipe> createRecipe(@RequestBody Recipe recipe) {
        RecipeDomainObject recipeDomainObject = recipeMapper.map_RecipeDTO_to_RecipeDO(recipe);
        recipeService.createRecipe(recipeDomainObject);
        Recipe savedRecipe = recipeMapper.map_RecipeDO_to_RecipeDTO(recipeDomainObject);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedRecipe);
    }

    @GetMapping
    public ResponseEntity<List<Recipe>> getAllRecipes() {
        List<Recipe> recipes = recipeService.getAllRecipes().stream()
                .map(recipeMapper::map_RecipeDO_to_RecipeDTO)
                .toList();
        return ResponseEntity.ok(recipes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Recipe> getRecipeById(@PathVariable Long id) {
        Recipe recipe = recipeMapper.map_RecipeDO_to_RecipeDTO(recipeService.getRecipeById(id));
        return ResponseEntity.ok(recipe);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Recipe> updateRecipe(@PathVariable Long id, @RequestBody Recipe recipe) {
        RecipeDomainObject recipeDomainObject = recipeMapper.map_RecipeDTO_to_RecipeDO(recipe);
        recipeService.updateRecipe(id, recipeDomainObject);
        Recipe updatedRecipe = recipeMapper.map_RecipeDO_to_RecipeDTO(recipeDomainObject);
        return ResponseEntity.ok(updatedRecipe);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecipe(@PathVariable Long id) throws NoSuchElementException {
        recipeService.deleteRecipe(id);
        return ResponseEntity.noContent().build();
    }
}
