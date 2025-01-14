package com.dvtsoftware.mealgen.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import com.dvtsoftware.mealgen.mapper.MealMapper;
import com.dvtsoftware.mealgen.model.domain.MealDomainObject;
import com.dvtsoftware.mealgen.model.domain.MealRequestDomainObject;
import com.dvtsoftware.mealgen.model.entity.MealEntity;
import com.dvtsoftware.mealgen.openai.MealGenerationService;
import com.dvtsoftware.mealgen.repository.MealRepository;
import io.awspring.cloud.sns.core.SnsTemplate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class MealServiceImplTest {

    @Mock
    private MealRepository mealRepository;

    @Mock
    private MealMapper mealMapper;

    @Mock
    private MealGenerationService mealGenerationService;

    @Mock
    private SnsTemplate snsTemplate;

    @InjectMocks
    private MealServiceImpl mealServiceImpl;

    private AutoCloseable mocks;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        mocks.close();
    }

    @Test
    void generateMeal_ShouldGenerateAndSaveMeal() throws IOException {
        MealRequestDomainObject mealRequest = new MealRequestDomainObject();
        MealDomainObject generatedMeal = new MealDomainObject();
        generatedMeal.setMealName("Test Meal");

        MealEntity mealEntity = new MealEntity();

        when(mealGenerationService.generateMeal(mealRequest)).thenReturn(generatedMeal);
        when(mealMapper.mapMealDOToMealEntity(generatedMeal)).thenReturn(mealEntity);

        MealDomainObject result = mealServiceImpl.generateMeal(mealRequest);

        verify(mealRepository).save(mealEntity);
        verify(snsTemplate).sendNotification("MealTopic", generatedMeal, "New Meal Created");
        assertEquals("Test Meal", result.getMealName());
    }

    @Test
    void getAllMeals_ShouldReturnAllMeals() {
        MealEntity mealEntity = new MealEntity();
        MealDomainObject mealDomainObject = new MealDomainObject();
        when(mealRepository.findAll()).thenReturn(List.of(mealEntity));
        when(mealMapper.mapMealEntityToMealDO(mealEntity)).thenReturn(mealDomainObject);

        List<MealDomainObject> meals = mealServiceImpl.getAllMeals();

        assertEquals(1, meals.size());
        verify(mealRepository).findAll();
        verify(mealMapper).mapMealEntityToMealDO(mealEntity);
    }

    @Test
    void getMealById_ShouldReturnMealWhenExists() {
        Long mealId = 1L;
        MealEntity mealEntity = new MealEntity();
        MealDomainObject mealDomainObject = new MealDomainObject();
        when(mealRepository.findById(mealId)).thenReturn(Optional.of(mealEntity));
        when(mealMapper.mapMealEntityToMealDO(mealEntity)).thenReturn(mealDomainObject);

        MealDomainObject result = mealServiceImpl.getMealById(mealId);

        assertNotNull(result);
        verify(mealRepository).findById(mealId);
        verify(mealMapper).mapMealEntityToMealDO(mealEntity);
    }

    @Test
    void getMealById_ShouldThrowExceptionWhenNotFound() {
        Long mealId = 1L;
        when(mealRepository.findById(mealId)).thenReturn(Optional.empty());

        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> mealServiceImpl.getMealById(mealId));
        assertEquals("Meal not found", exception.getMessage());
        verify(mealRepository).findById(mealId);
    }

    @Test
    void updateMeal_ShouldUpdateMeal() {
        Long mealId = 1L;
        MealDomainObject mealDomainObject = new MealDomainObject();
        MealEntity updatedMealEntity = new MealEntity();

        when(mealRepository.existsById(mealId)).thenReturn(true);
        when(mealMapper.mapMealDOToMealEntity(mealDomainObject)).thenReturn(updatedMealEntity);

        mealServiceImpl.updateMeal(mealId, mealDomainObject);

        verify(mealRepository).existsById(mealId);
        verify(mealMapper).mapMealDOToMealEntity(mealDomainObject);
        verify(mealRepository).save(updatedMealEntity);
    }

    @Test
    void deleteMeal_ShouldDeleteMeal() {
        Long mealId = 1L;

        mealServiceImpl.deleteMeal(mealId);

        verify(mealRepository).deleteById(mealId);
    }

    @Test
    void deleteAllMeals_ShouldDeleteAllMeals() {
        mealServiceImpl.deleteAllMeals();

        verify(mealRepository).deleteAll();
    }

    @Test
    void sendMealNotification_ShouldSendNotification() {
        MealDomainObject mealDomainObject = new MealDomainObject();
        mealDomainObject.setMealName("Test Meal");

        mealServiceImpl.sendMealNotification(mealDomainObject);

        verify(snsTemplate).sendNotification("MealTopic", mealDomainObject, "New Meal Created");
    }
}
