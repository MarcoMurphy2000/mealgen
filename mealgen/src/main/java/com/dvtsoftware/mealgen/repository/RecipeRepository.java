package com.dvtsoftware.mealgen.repository;

import com.dvtsoftware.mealgen.model.entity.RecipeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeRepository extends JpaRepository<RecipeEntity, Long> {

}
