package com.dvtsoftware.mealgen.repository;

import com.dvtsoftware.mealgen.model.entity.MealEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MealRepository extends JpaRepository<MealEntity, Long> {

}
