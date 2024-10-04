package com.dvtsoftware.mealgen.controller;

import java.util.List;
import java.util.NoSuchElementException;

import com.dvtsoftware.mealgen.generated.model.Meal;
import com.dvtsoftware.mealgen.mapper.MealMapper;
import com.dvtsoftware.mealgen.service.MealService;
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

    private final MealService mealService;
    private final MealMapper mealMapper;

    @Autowired
    public MealController(MealService mealService, MealMapper mealMapper) {
        this.mealService = mealService;
        this.mealMapper = mealMapper;
    }

    @PostMapping("/generate")
    public ResponseEntity<Meal> generateMeal(@RequestBody Meal meal) {
        //Map from DTO to Domain Object
        mealService.generateMeal(mealMapper.map_MealDTO_to_MealDO(meal));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<Meal>> getAllMeals() {
        List<Meal> meals = mealService.getAllMeals().stream()
                .map(mealMapper::map_MealDO_to_MealDTO)
                .toList();
        return ResponseEntity.ok(meals);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Meal> getMealById(@PathVariable Long id) {
        Meal meal = mealMapper.map_MealDO_to_MealDTO(mealService.getMealById(id));
        return ResponseEntity.ok(meal);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateMeal(@PathVariable Long id, @RequestBody Meal meal) {
        mealService.updateMeal(id, mealMapper.map_MealDTO_to_MealDO(meal));
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMeal(@PathVariable Long id) throws NoSuchElementException {
        mealService.deleteMeal(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAllMeals() {
        mealService.deleteAllMeals();
        return ResponseEntity.noContent().build();
    }
}
