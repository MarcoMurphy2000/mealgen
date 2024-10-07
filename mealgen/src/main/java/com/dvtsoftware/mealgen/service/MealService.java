package com.dvtsoftware.mealgen.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import com.dvtsoftware.mealgen.mapper.MealMapper;
import com.dvtsoftware.mealgen.model.domain.MealDomainObject;
import com.dvtsoftware.mealgen.model.domain.MealRequestDomainObject;
import com.dvtsoftware.mealgen.model.domain.RecipeDomainObject;
import com.dvtsoftware.mealgen.model.entity.MealEntity;
import com.dvtsoftware.mealgen.repository.MealRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MealService {

    private static final String MEAL_NOT_FOUND = "Meal not found";

    private final MealRepository mealRepository;
    private final MealMapper mealMapper;
    private final OpenAIService openAIService;

    @Autowired
    public MealService(MealRepository mealRepository, MealMapper mealMapper, OpenAIService openAIService) {
        this.mealRepository = mealRepository;
        this.mealMapper = mealMapper;
        this.openAIService = openAIService;
    }

    public MealDomainObject generateMeal(MealRequestDomainObject mealRequestDomainObject) throws IOException {
        // Construct the prompt using the mealRequestDomainObject data
        String prompt = constructOpenAIPrompt(mealRequestDomainObject);

        // Call OpenAI API to generate the meal
        String openAIResponse = openAIService.openAI(prompt);

        // Process OpenAI response and convert to MealDomainObject
        MealDomainObject mealDomainObject = processOpenAIResponse(openAIResponse);

        // Save the meal to the database
        MealEntity mealEntity = mealMapper.mapMealDOToMealEntity(mealDomainObject);
        mealRepository.save(mealEntity);

        // Return the generated meal
        return mealDomainObject;
    }

    private String constructOpenAIPrompt(MealRequestDomainObject mealRequestDomainObject) {
        StringBuilder promptBuilder = new StringBuilder();
        promptBuilder.append("User provided input:\n")
                .append("Ingredients (comma-separated): ")
                .append(String.join(", ", mealRequestDomainObject.getIngredients())).append(".\n")
                .append("Allow extra ingredients: ").append(mealRequestDomainObject.getAllowExtraIngredients() ? "Yes" : "No").append(".\n")
                .append("Gender: ").append(mealRequestDomainObject.getGender()).append(".\n")
                .append("Goal: ").append(mealRequestDomainObject.getMealCategory()).append(".\n");

        // Specify the format for OpenAI's output, including ingredient list, macro breakdown, and calories
        promptBuilder.append("Generate a meal with the following format in JSON:\n")
                .append("{\n")
                .append("  \"recipe\": {\n")
                .append("    \"recipeName\": \"<recipe_name>\",\n")
                .append("    \"ingredients\": [\"<ingredient1>\", \"<ingredient2>\", ...],\n")
                .append("    \"steps\": [\"<step1>\", \"<step2>\", ...]\n")
                .append("  },\n")
                .append("  \"nutritional_values\": {\n")
                .append("    \"protein_percentage\": \"<percentage>\",\n")
                .append("    \"carbohydrates_percentage\": \"<percentage>\",\n")
                .append("    \"fat_percentage\": \"<percentage>\",\n")
                .append("    \"calories\": \"<calories>\"\n")
                .append("  },\n")
                .append("  \"category\": \"<meal_category>\"\n")
                .append("}");

        // Append macronutrient breakdown guidelines based on gender and goal
        appendMacroGuidelines(promptBuilder, mealRequestDomainObject);

        return promptBuilder.toString();
    }

    private void appendMacroGuidelines(StringBuilder promptBuilder, MealRequestDomainObject mealRequestDomainObject) {
        if (mealRequestDomainObject.getGender().equalsIgnoreCase("Female")) {
            switch (mealRequestDomainObject.getMealCategory()) {
                case "WeightLoss":
                    promptBuilder.append("Ensure macronutrient breakdown falls between:\n")
                            .append("Protein: 25–30%, Carbohydrates: 30–40%, Fat: 30–35%.\n");
                    break;
                case "MuscleGain":
                    promptBuilder.append("Ensure macronutrient breakdown falls between:\n")
                            .append("Protein: 25–30%, Carbohydrates: 45–55%, Fat: 25–30%.\n");
                    break;
                case "Maintenance":
                    promptBuilder.append("Ensure macronutrient breakdown falls between:\n")
                            .append("Protein: 20–25%, Carbohydrates: 45–50%, Fat: 30–35%.\n");
                    break;
            }
        } else if (mealRequestDomainObject.getGender().equalsIgnoreCase("Male")) {
            switch (mealRequestDomainObject.getMealCategory()) {
                case "WeightLoss":
                    promptBuilder.append("Ensure macronutrient breakdown falls between:\n")
                            .append("Protein: 30–35%, Carbohydrates: 25–35%, Fat: 30–35%.\n");
                    break;
                case "MuscleGain":
                    promptBuilder.append("Ensure macronutrient breakdown falls between:\n")
                            .append("Protein: 30–35%, Carbohydrates: 50–60%, Fat: 20–25%.\n");
                    break;
                case "Maintenance":
                    promptBuilder.append("Ensure macronutrient breakdown falls between:\n")
                            .append("Protein: 25–30%, Carbohydrates: 50–55%, Fat: 25–30%.\n");
                    break;
            }
        }
    }

    private MealDomainObject processOpenAIResponse(String response) {
        try {
            // Parse the raw response string as a JSONObject
            JSONObject jsonResponse = new JSONObject(response);

            // Get the 'choices' array from the response
            JSONArray choices = jsonResponse.getJSONArray("choices");

            // Get the first choice object
            JSONObject firstChoice = choices.getJSONObject(0);

            // Get the 'content' field from the message, which contains the assistant's plain-text response
            String content = firstChoice.getJSONObject("message").getString("content");

            // Now parse the content as JSON (since it is returned as a plain-text string)
            JSONObject mealJson = new JSONObject(content);

            // Create a MealDomainObject to store the parsed data
            MealDomainObject mealDomainObject = new MealDomainObject();
            RecipeDomainObject recipe = new RecipeDomainObject();

            // Extract recipe details from the JSON
            JSONObject recipeJson = mealJson.getJSONObject("recipe");
            recipe.setRecipeName(recipeJson.getString("recipeName"));

            // Extract and map recipe ingredients
            JSONArray ingredientsArray = recipeJson.getJSONArray("ingredients");
            List<String> ingredients = new ArrayList<>();
            for (int i = 0; i < ingredientsArray.length(); i++) {
                ingredients.add(ingredientsArray.getString(i));
            }
            mealDomainObject.setIngredients(ingredients);

            // Extract and map recipe steps
            JSONArray stepsArray = recipeJson.getJSONArray("steps");
            List<String> steps = new ArrayList<>();
            for (int i = 0; i < stepsArray.length(); i++) {
                steps.add(stepsArray.getString(i));
            }
            recipe.setSteps(steps);

            // Set the recipe in the MealDomainObject
            mealDomainObject.setRecipe(recipe);

            // Extract macronutrient breakdown and calories
            JSONObject nutritionalValues = mealJson.getJSONObject("nutritional_values");
            mealDomainObject.setProtein(Double.parseDouble(nutritionalValues.getString("protein_percentage").replace("%", "")));
            mealDomainObject.setCarbohydrates(Double.parseDouble(nutritionalValues.getString("carbohydrates_percentage").replace("%", "")));
            mealDomainObject.setFat(Double.parseDouble(nutritionalValues.getString("fat_percentage").replace("%", "")));
            mealDomainObject.setCalories(Double.parseDouble(nutritionalValues.getString("calories")));

            // Set the category
            mealDomainObject.setCategory(mealJson.getString("category"));

            return mealDomainObject;

        } catch (Exception e) {
            // Log the error and the raw response for debugging
            System.err.println("Error processing OpenAI response: " + e.getMessage());
            System.err.println("Raw Response: " + response);
            throw new RuntimeException("Failed to process OpenAI response");
        }
    }

    private MealDomainObject parseMealContent(String content) {
        MealDomainObject mealDomainObject = new MealDomainObject();
        RecipeDomainObject recipe = new RecipeDomainObject();

        // Split the content by lines for easier processing
        String[] lines = content.split("\n");

        // Extract the recipe name and other details
        for (String line : lines) {
            if (line.startsWith("Recipe:")) {
                String recipeName = line.replace("Recipe:", "").trim();
                recipe.setRecipeName(recipeName);
            }
            if (line.contains("Protein:")) {
                String protein = line.split(":")[1].trim().split(" ")[0]; // Extract the numeric value
                mealDomainObject.setProtein(Double.parseDouble(protein));
            }
            if (line.contains("Carbohydrates:")) {
                String carbs = line.split(":")[1].trim().split(" ")[0]; // Extract the numeric value
                mealDomainObject.setCarbohydrates(Double.parseDouble(carbs));
            }
            if (line.contains("Fat:")) {
                String fat = line.split(":")[1].trim().split(" ")[0]; // Extract the numeric value
                mealDomainObject.setFat(Double.parseDouble(fat));
            }
            if (line.contains("Total calories:")) {
                String calories = line.split(":")[1].trim().split(" ")[0]; // Extract the numeric value
                mealDomainObject.setCalories(Double.parseDouble(calories));
            }
        }

        // Set the parsed recipe and category in the MealDomainObject
        recipe.setSteps(Arrays.asList("Cook the chicken", "Steam the broccoli", "Cook the rice", "Mix together")); // Example steps
        mealDomainObject.setRecipe(recipe);
        mealDomainObject.setCategory("Muscle Gain");

        return mealDomainObject;
    }

    public List<MealDomainObject> getAllMeals() {
        List<MealEntity> mealEntities = mealRepository.findAll();
        return mealEntities.stream()
                .map(mealMapper::mapMealEntityToMealDO)
                .toList();
    }

    public MealDomainObject getMealById(final Long id) throws NoSuchElementException {
        Optional<MealEntity> mealEntity = mealRepository.findById(id);
        return mealEntity.map(mealMapper::mapMealEntityToMealDO)
                .orElseThrow(() -> new RuntimeException(MEAL_NOT_FOUND));
    }

    public void updateMeal(Long id, MealDomainObject mealDomainObject) throws NoSuchElementException {
        checkMealExists(id);
        MealEntity updatedMealEntity = mealMapper.mapMealDOToMealEntity(mealDomainObject);
        updatedMealEntity.setId(id);
        mealRepository.save(updatedMealEntity);
    }

    public void deleteMeal(Long id) {
        mealRepository.deleteById(id);
    }

    public void deleteAllMeals() {
        mealRepository.deleteAll();
    }

    public void checkMealExists(final Long id) throws NoSuchElementException {
        if (!mealRepository.existsById(id)) {
            throw new NoSuchElementException(MEAL_NOT_FOUND);
        }
    }
}
