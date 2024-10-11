package com.dvtsoftware.mealgen.mapper;

import com.dvtsoftware.mealgen.generated.model.MealRequest;
import com.dvtsoftware.mealgen.model.domain.MealRequestDomainObject;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MealRequestMapper {

    //Note - Used in Controller Layer (API layer) - Mapping between DTO and Domain Object to send to Service Layer - Domain Object goes to Service Layer.
    MealRequestDomainObject mapMealRequestDTOToMealRequestDO(MealRequest mealRequest);
}
