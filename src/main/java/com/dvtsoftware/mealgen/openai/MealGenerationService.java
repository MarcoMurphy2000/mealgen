package com.dvtsoftware.mealgen.openai;

import java.io.IOException;

import com.dvtsoftware.mealgen.mapper.MealMapper;
import com.dvtsoftware.mealgen.model.domain.MealDomainObject;
import com.dvtsoftware.mealgen.model.domain.MealRequestDomainObject;
import com.dvtsoftware.mealgen.model.entity.MealEntity;
import com.dvtsoftware.mealgen.repository.MealRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MealGenerationService {

    private final MealRepository mealRepository;
    private final MealMapper mealMapper;
    private final OpenAIService openAIService;

    @Autowired
    public MealGenerationService(MealRepository mealRepository, MealMapper mealMapper, OpenAIService openAIService) {
        this.mealRepository = mealRepository;
        this.mealMapper = mealMapper;
        this.openAIService = openAIService;
    }

    public MealDomainObject generateMeal(MealRequestDomainObject mealRequestDomainObject) throws IOException {
        // Construct the OpenAI prompt with all necessary details and JSON output format
        String prompt = constructOpenAIPrompt(mealRequestDomainObject);

        // Call OpenAI API to generate a response
        String openAIResponse = openAIService.generateResponse(prompt);

        // Parse the OpenAI response into a MealDomainObject
        MealDomainObject mealDomainObject = new OpenAIResponseParser().parse(openAIResponse);

        // Map MealDomainObject to MealEntity and save to the database
        MealEntity mealEntity = mealMapper.mapMealDOToMealEntity(mealDomainObject);
        mealRepository.save(mealEntity);

        // Return the MealDomainObject
        return mealDomainObject;
    }

    private String constructOpenAIPrompt(MealRequestDomainObject mealRequestDomainObject) {
        return new OpenAIPromptBuilder()
                .withIngredients(mealRequestDomainObject.getIngredients())
                .withAllowExtraIngredients(mealRequestDomainObject.getAllowExtraIngredients())
                .withGender(mealRequestDomainObject.getGender())
                .withMealCategory(String.valueOf(mealRequestDomainObject.getMealCategory()))
                .withMacroGuidelines(mealRequestDomainObject)
                .withJSONFormat()
                .build();
    }
}
