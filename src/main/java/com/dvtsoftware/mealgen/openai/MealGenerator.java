package com.dvtsoftware.mealgen.openai;

import java.io.IOException;

import com.dvtsoftware.mealgen.model.domain.MealDomainObject;
import com.dvtsoftware.mealgen.model.domain.MealRequestDomainObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MealGenerator {

    private final OpenAIClient openAIClient;

    @Autowired
    public MealGenerator(OpenAIClient openAIClient) {
        this.openAIClient = openAIClient;
    }

    public MealDomainObject generateMeal(MealRequestDomainObject mealRequestDomainObject) throws IOException {
        String prompt = constructOpenAIPrompt(mealRequestDomainObject);

        String openAIResponse = openAIClient.generateResponse(prompt);

        return new OpenAIResponseParser().parse(openAIResponse);
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
