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
    private final MealRequestMapper mealRequestMapper;

    @Autowired
    public MealController(MealService mealService, MealMapper mealMapper, MealRequestMapper mealRequestMapper) {
        this.mealService = mealService;
        this.mealMapper = mealMapper;
        this.mealRequestMapper = mealRequestMapper;
    }

//    @PostMapping("/generate")
//    public ResponseEntity<MealRequest> generateMeal(@RequestBody MealRequest mealRequest) throws IOException {
//        //Map from DTO to Domain Object
//        mealService.generateMeal(mealRequestMapper.mapMealRequestDTOToMealRequestDO(mealRequest));
//        return ResponseEntity.status(HttpStatus.CREATED).build();
//    }

    @PostMapping("/generate")
    public ResponseEntity<Meal> generateMeal(@RequestBody MealRequest mealRequestDTO) throws IOException {
        // Map from DTO to Domain Object
        MealRequestDomainObject mealRequestDomainObject = mealRequestMapper.mapMealRequestDTOToMealRequestDO(mealRequestDTO);

        // Generate the meal using the service
        MealDomainObject generatedMeal = mealService.generateMeal(mealRequestDomainObject);

        // Map the generated MealDomainObject to MealDTO
        Meal mealDTO = mealMapper.mapMealDOToMealDTO(generatedMeal);

        // Return the generated meal in the response
        return ResponseEntity.status(HttpStatus.CREATED).body(mealDTO);
    }

    @GetMapping
    public ResponseEntity<List<Meal>> getAllMeals() {
        List<Meal> meals = mealService.getAllMeals().stream()
                .map(mealMapper::mapMealDOToMealDTO)
                .toList();
        return ResponseEntity.ok(meals);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Meal> getMealById(@PathVariable Long id) {
        Meal meal = mealMapper.mapMealDOToMealDTO(mealService.getMealById(id));
        return ResponseEntity.ok(meal);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateMeal(@PathVariable Long id, @RequestBody Meal meal) {
        mealService.updateMeal(id, mealMapper.mapMealDTOToMealDO(meal));
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
