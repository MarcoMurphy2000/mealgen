package com.dvtsoftware.mealgen.mapper;

import com.dvtsoftware.mealgen.generated.model.Meal;
import com.dvtsoftware.mealgen.model.domain.MealDomainObject;
import com.dvtsoftware.mealgen.model.entity.MealEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MealMapper {

    //Note - Used in Controller Layer (API layer) - Mapping between DTO and Domain Object to send to Service Layer - Domain Object goes to Service Layer.
    MealDomainObject mapMealDTOToMealDO(Meal meal);

    Meal mapMealDOToMealDTO(MealDomainObject mealDomainObject);

    //Note - Used in Service Layer (Business Logic Layer) - Mapping between Domain Object and Entity to send to Repository Layer - Entity goes to Repository Layer
    MealDomainObject mapMealEntityToMealDO(MealEntity mealEntity);

    MealEntity mapMealDOToMealEntity(MealDomainObject mealDomainObject);
}
