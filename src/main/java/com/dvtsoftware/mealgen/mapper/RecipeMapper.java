package com.dvtsoftware.mealgen.mapper;

import com.dvtsoftware.mealgen.generated.model.Recipe;
import com.dvtsoftware.mealgen.model.domain.RecipeDomainObject;
import com.dvtsoftware.mealgen.model.entity.RecipeEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RecipeMapper {

    //Used in Controller Layer (API layer) - Mapping between DTO and Domain Object to send to Service Layer - Domain Object goes to Service Layer.
    RecipeDomainObject mapRecipeDTOToRecipeDO(Recipe recipe);

    Recipe mapRecipeDOToRecipeDTO(RecipeDomainObject recipeDomainObject);

    //Used in Service Layer (Business Logic Layer) - Mapping between Domain Object and Entity to send to Repository Layer - Entity goes to Repository Layer
    RecipeDomainObject mapRecipeEntityToRecipeDO(RecipeEntity recipeEntity);

    RecipeEntity mapRecipeDOToRecipeEntity(RecipeDomainObject recipeDomainObject);

}
