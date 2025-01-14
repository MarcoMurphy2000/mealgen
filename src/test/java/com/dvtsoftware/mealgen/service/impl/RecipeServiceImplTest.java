package com.dvtsoftware.mealgen.service.impl;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import com.dvtsoftware.mealgen.mapper.RecipeMapper;
import com.dvtsoftware.mealgen.model.domain.RecipeDomainObject;
import com.dvtsoftware.mealgen.model.entity.RecipeEntity;
import com.dvtsoftware.mealgen.repository.RecipeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RecipeServiceImplTest {

    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private RecipeMapper recipeMapper;

    @InjectMocks
    private RecipeServiceImpl recipeServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllRecipes_ShouldReturnAllRecipes() {
        RecipeEntity recipeEntity = new RecipeEntity();
        RecipeDomainObject recipeDomainObject = new RecipeDomainObject();

        when(recipeRepository.findAll()).thenReturn(List.of(recipeEntity));
        when(recipeMapper.mapRecipeEntityToRecipeDO(recipeEntity)).thenReturn(recipeDomainObject);

        List<RecipeDomainObject> recipes = recipeServiceImpl.getAllRecipes();

        assertNotNull(recipes);
        assertEquals(1, recipes.size());
        verify(recipeRepository).findAll();
        verify(recipeMapper).mapRecipeEntityToRecipeDO(recipeEntity);
    }

    @Test
    void getRecipeById_ShouldReturnRecipeWhenExists() {
        Long recipeId = 1L;
        RecipeEntity recipeEntity = new RecipeEntity();
        RecipeDomainObject recipeDomainObject = new RecipeDomainObject();

        when(recipeRepository.findById(recipeId)).thenReturn(Optional.of(recipeEntity));
        when(recipeMapper.mapRecipeEntityToRecipeDO(recipeEntity)).thenReturn(recipeDomainObject);

        RecipeDomainObject result = recipeServiceImpl.getRecipeById(recipeId);

        assertNotNull(result);
        verify(recipeRepository).findById(recipeId);
        verify(recipeMapper).mapRecipeEntityToRecipeDO(recipeEntity);
    }

    @Test
    void getRecipeById_ShouldThrowExceptionWhenNotFound() {
        Long recipeId = 1L;

        when(recipeRepository.findById(recipeId)).thenReturn(Optional.empty());

        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> recipeServiceImpl.getRecipeById(recipeId));
        assertEquals("Recipe not found", exception.getMessage());
        verify(recipeRepository).findById(recipeId);
    }

    @Test
    void updateRecipe_ShouldUpdateRecipe() {
        Long recipeId = 1L;
        RecipeDomainObject recipeDomainObject = new RecipeDomainObject();
        RecipeEntity recipeEntity = new RecipeEntity();

        when(recipeRepository.existsById(recipeId)).thenReturn(true);
        when(recipeMapper.mapRecipeDOToRecipeEntity(recipeDomainObject)).thenReturn(recipeEntity);

        recipeServiceImpl.updateRecipe(recipeId, recipeDomainObject);

        verify(recipeRepository).existsById(recipeId);
        verify(recipeMapper).mapRecipeDOToRecipeEntity(recipeDomainObject);
        verify(recipeRepository).save(recipeEntity);
    }

    @Test
    void updateRecipe_ShouldThrowExceptionWhenRecipeDoesNotExist() {
        Long recipeId = 1L;
        RecipeDomainObject recipeDomainObject = new RecipeDomainObject();

        when(recipeRepository.existsById(recipeId)).thenReturn(false);

        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> recipeServiceImpl.updateRecipe(recipeId, recipeDomainObject));
        assertEquals("Recipe not found", exception.getMessage());
        verify(recipeRepository).existsById(recipeId);
        verify(recipeMapper, never()).mapRecipeDOToRecipeEntity(any());
        verify(recipeRepository, never()).save(any());
    }

    @Test
    void deleteRecipe_ShouldDeleteRecipe() {
        Long recipeId = 1L;

        recipeServiceImpl.deleteRecipe(recipeId);

        verify(recipeRepository).deleteById(recipeId);
    }

    @Test
    void checkRecipeExists_ShouldNotThrowExceptionWhenRecipeExists() {
        Long recipeId = 1L;
        when(recipeRepository.existsById(recipeId)).thenReturn(true);

        recipeServiceImpl.checkRecipeExists(recipeId);

        verify(recipeRepository).existsById(recipeId);
    }

    @Test
    void checkRecipeExists_ShouldThrowExceptionWhenRecipeDoesNotExist() {
        Long recipeId = 1L;
        when(recipeRepository.existsById(recipeId)).thenReturn(false);

        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> recipeServiceImpl.checkRecipeExists(recipeId));
        assertEquals("Recipe not found", exception.getMessage());
        verify(recipeRepository).existsById(recipeId);
    }
}
