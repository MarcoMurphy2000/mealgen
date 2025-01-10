package com.dvtsoftware.mealgen.service.interfaces;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

import com.dvtsoftware.mealgen.model.domain.MealDomainObject;
import com.dvtsoftware.mealgen.model.domain.MealRequestDomainObject;

public interface MealService {

    MealDomainObject generateMeal(MealRequestDomainObject mealRequestDomainObject) throws IOException;

    List<MealDomainObject> getAllMeals();

    MealDomainObject getMealById(Long id) throws NoSuchElementException;

    void updateMeal(Long id, MealDomainObject mealDomainObject) throws NoSuchElementException;

    void deleteMeal(Long id);

    void deleteAllMeals();

    void sendMealNotification(MealDomainObject mealDomainObject);
}

