package com.dvtsoftware.mealgen.mapper;

import com.dvtsoftware.mealgen.generated.model.Meal;
import com.dvtsoftware.mealgen.model.domain.MealDomainObject;
import com.dvtsoftware.mealgen.model.entity.MealEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MealMapper {

    //Used in Controller Layer (API layer) - Mapping between DTO and Domain Object to send to Service Layer - Domain Object goes to Service Layer.
    MealDomainObject map_MealDTO_to_MealDO(Meal meal);

    Meal map_MealDO_to_MealDTO(MealDomainObject mealDomainObject);

    //Used in Service Layer (Business Logic Layer) - Mapping between Domain Object and Entity to send to Repository Layer - Entity goes to Repository Layer
    MealDomainObject map_MealEntity_to_MealDO(MealEntity mealEntity);

    MealEntity map_MealDO_to_MealEntity(MealDomainObject mealDomainObject);
}
