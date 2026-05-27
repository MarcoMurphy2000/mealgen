package com.dvtsoftware.mealgen.openai;

import java.util.ArrayList;
import java.util.List;

import com.dvtsoftware.mealgen.model.domain.MealDomainObject;
import com.dvtsoftware.mealgen.model.domain.RecipeDomainObject;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OpenAIResponseParser {

    private static final Logger log = LoggerFactory.getLogger(OpenAIResponseParser.class);

    public MealDomainObject parse(String response) {
        try {
            // Log the raw response for debugging purposes
            log.debug("Raw JSON response from OpenAI: {}", response);

            // Parse the response as a JSON object
            JSONObject jsonResponse = new JSONObject(response);
            JSONArray choices = jsonResponse.getJSONArray("choices");
            String content = choices.getJSONObject(0).getJSONObject("message").getString("content");

            // Parse the content as JSON
            JSONObject mealJson = new JSONObject(content);

            // Create a MealDomainObject to store the parsed data
            MealDomainObject mealDomainObject = new MealDomainObject();
            RecipeDomainObject recipe = new RecipeDomainObject();

            // Extract the meal name from the JSON
            String mealName = mealJson.getString("mealName");
            mealDomainObject.setMealName(mealName);

            // Extract and map recipe ingredients
            JSONArray ingredientsArray = mealJson.getJSONArray("ingredients");
            List<String> ingredients = new ArrayList<>();
            for (int i = 0; i < ingredientsArray.length(); i++) {
                ingredients.add(ingredientsArray.getString(i));
            }
            mealDomainObject.setIngredients(ingredients);

            // Extract and map recipe steps
            JSONArray stepsArray = mealJson.getJSONArray("steps");
            List<String> steps = new ArrayList<>();
            for (int i = 0; i < stepsArray.length(); i++) {
                steps.add(stepsArray.getString(i));
            }
            recipe.setSteps(steps);

            // Extract macronutrient breakdown and calories
            JSONObject nutritionalValues = mealJson.getJSONObject("nutritionalValues");
            mealDomainObject.setProtein(Double.parseDouble(nutritionalValues.getString("protein_percentage").replace("%", "")));
            mealDomainObject.setCarbohydrates(Double.parseDouble(nutritionalValues.getString("carbohydrates_percentage").replace("%", "")));
            mealDomainObject.setFat(Double.parseDouble(nutritionalValues.getString("fat_percentage").replace("%", "")));
            mealDomainObject.setCalories(Double.parseDouble(nutritionalValues.getString("calories")));

            // Extract and set the category from the response
            String category = mealJson.getString("category");
            mealDomainObject.setCategory(category);

            // Set the recipe in the meal domain object
            mealDomainObject.setRecipe(recipe);

            return mealDomainObject;

        } catch (JSONException e) {
            // Log the error with the response
            log.error("Error parsing OpenAI response: {}", e.getMessage());
            log.debug("Raw response: {}", response);
            throw new RuntimeException("Error parsing OpenAI response", e);
        }
    }
}
