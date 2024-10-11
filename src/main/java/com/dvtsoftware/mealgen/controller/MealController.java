package com.dvtsoftware.mealgen.controller;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

import com.dvtsoftware.mealgen.generated.model.Meal;
import com.dvtsoftware.mealgen.generated.model.MealRequest;
import com.dvtsoftware.mealgen.mapper.MealMapper;
import com.dvtsoftware.mealgen.mapper.MealRequestMapper;
import com.dvtsoftware.mealgen.model.domain.MealDomainObject;
import com.dvtsoftware.mealgen.model.domain.MealRequestDomainObject;
import com.dvtsoftware.mealgen.service.impl.MealServiceImpl;
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
@RequestMapping("/meals")
public class MealController {

    private final MealServiceImpl mealServiceImpl;
    private final MealMapper mealMapper;
    private final MealRequestMapper mealRequestMapper;

    @Autowired
    public MealController(MealServiceImpl mealServiceImpl, MealMapper mealMapper, MealRequestMapper mealRequestMapper) {
        this.mealServiceImpl = mealServiceImpl;
        this.mealMapper = mealMapper;
        this.mealRequestMapper = mealRequestMapper;
    }

    @PostMapping("/generate")
    public ResponseEntity<Meal> generateMeal(@RequestBody MealRequest mealRequestDTO) throws IOException {
        MealRequestDomainObject mealRequestDomainObject = mealRequestMapper.mapMealRequestDTOToMealRequestDO(mealRequestDTO);

        MealDomainObject generatedMeal = mealServiceImpl.generateMeal(mealRequestDomainObject);

        Meal mealDTO = mealMapper.mapMealDOToMealDTO(generatedMeal);

        return ResponseEntity.status(HttpStatus.CREATED).body(mealDTO);
    }

    @GetMapping
    public ResponseEntity<List<Meal>> getAllMeals() {
        List<Meal> meals = mealServiceImpl.getAllMeals().stream()
                .map(mealMapper::mapMealDOToMealDTO)
                .toList();
        return ResponseEntity.ok(meals);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Meal> getMealById(@PathVariable Long id) {
        Meal meal = mealMapper.mapMealDOToMealDTO(mealServiceImpl.getMealById(id));
        return ResponseEntity.ok(meal);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateMeal(@PathVariable Long id, @RequestBody Meal meal) {
        mealServiceImpl.updateMeal(id, mealMapper.mapMealDTOToMealDO(meal));
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMeal(@PathVariable Long id) throws NoSuchElementException {
        mealServiceImpl.deleteMeal(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAllMeals() {
        mealServiceImpl.deleteAllMeals();
        return ResponseEntity.noContent().build();
    }
}
