package com.dvtsoftware.mealgen.repository;

import com.dvtsoftware.mealgen.model.entity.MealEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MealRepository extends JpaRepository<MealEntity, Long> {

    @EntityGraph(value = "MealDefault")
    @Query("SELECT m FROM MealEntity m WHERE m.id = :id")
    MealEntity findMealWithDefaultDetails(@Param("id") Long id);

    @EntityGraph(value = "MealWithAllDetails")
    @Query("SELECT m FROM MealEntity m WHERE m.id = :id")
    MealEntity findMealWithAllDetails(@Param("id") Long id);

}
