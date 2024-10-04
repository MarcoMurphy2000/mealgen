package com.dvtsoftware.mealgen.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import com.dvtsoftware.mealgen.mapper.MealMapper;
import com.dvtsoftware.mealgen.model.domain.MealDomainObject;
import com.dvtsoftware.mealgen.model.entity.MealEntity;
import com.dvtsoftware.mealgen.repository.MealRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MealService {

    private static final String MEAL_NOT_FOUND = "Meal not found";

    private final MealRepository mealRepository;
    private final MealMapper mealMapper;

    @Autowired
    public MealService(MealRepository mealRepository, MealMapper mealMapper) {
        this.mealRepository = mealRepository;
        this.mealMapper = mealMapper;
    }

    public void generateMeal(MealDomainObject mealDomainObject) {
        // Business logic to generate a mealEntity based on the ingredients and macronutrients
        // This is where you might integrate the OpenAI API to generate the mealEntity suggestion
        mealRepository.save(mealMapper.map_MealDO_to_MealEntity(mealDomainObject));
    }

    public List<MealDomainObject> getAllMeals() {
        List<MealEntity> mealEntities = mealRepository.findAll();
        return mealEntities.stream()
                .map(mealMapper::map_MealEntity_to_MealDO)
                .toList();
    }

    public MealDomainObject getMealById(final Long id) throws NoSuchElementException {
        Optional<MealEntity> mealEntity = mealRepository.findById(id);
        return mealEntity.map(mealMapper::map_MealEntity_to_MealDO)
                .orElseThrow(() -> new RuntimeException(MEAL_NOT_FOUND));
    }

    public MealEntity updateMeal(Long id, MealDomainObject mealDomainObject) throws NoSuchElementException {
        checkMealExists(id);
        MealEntity updatedMealEntity = mealMapper.map_MealDO_to_MealEntity(mealDomainObject);
        updatedMealEntity.setId(id);
        return mealRepository.save(updatedMealEntity);
    }

    public void deleteMeal(Long id) {
        mealRepository.deleteById(id);
    }

    public void deleteAllMeals() {
        mealRepository.deleteAll();
    }

    public void checkMealExists(final Long id) throws NoSuchElementException {
        if (!mealRepository.existsById(id)) {
            throw new NoSuchElementException(MEAL_NOT_FOUND);
        }
    }
}
