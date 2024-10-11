package com.dvtsoftware.mealgen.openai;

import java.util.List;

import com.dvtsoftware.mealgen.model.domain.MealRequestDomainObject;

public class OpenAIPromptBuilder {

    private final StringBuilder promptBuilder;

    public OpenAIPromptBuilder() {
        promptBuilder = new StringBuilder();
    }

    public OpenAIPromptBuilder withIngredients(List<String> ingredients) {
        promptBuilder.append("Ingredients (comma-separated): ")
                .append(String.join(", ", ingredients))
                .append(".\n");
        return this;
    }

    public OpenAIPromptBuilder withAllowExtraIngredients(Boolean allowExtraIngredients) {
        if (Boolean.TRUE.equals(allowExtraIngredients)) {
            promptBuilder.append("Allow extra ingredients: Yes. Add complementary ingredients to the meal.\n");
        } else {
            promptBuilder.append("Allow extra ingredients: No. Only use the provided ingredients.\n");
        }
        return this;
    }

    public OpenAIPromptBuilder withGender(String gender) {
        if (gender != null && !gender.isEmpty()) {
            promptBuilder.append("Gender: ").append(gender).append(".\n");
        } else {
            promptBuilder.append("Gender: Not specified (default guidelines applied).\n");
        }
        return this;
    }

    public OpenAIPromptBuilder withMealCategory(String mealCategory) {
        if (mealCategory != null && !mealCategory.isEmpty()) {
            promptBuilder.append("Meal category: ").append(mealCategory).append(".\n");
        } else {
            promptBuilder.append("Based on the macronutrient breakdown, assign the meal to one of the following categories: Weight Loss, Muscle Gain, or Maintenance.\n");
        }
        return this;
    }

    public OpenAIPromptBuilder withMacroGuidelines(MealRequestDomainObject mealRequestDomainObject) {
        String gender = mealRequestDomainObject.getGender();
        String mealCategory = String.valueOf(mealRequestDomainObject.getMealCategory());

        // If no gender is provided, use the default average macronutrient breakdown
        if (gender == null || gender.isEmpty()) {
            handleDefaultMacros(mealCategory);
        } else {
            // Handle gender-specific macronutrient breakdowns
            switch (gender.toLowerCase()) {
                case "female":
                    handleFemaleMacros(mealCategory);
                    break;
                case "male":
                    handleMaleMacros(mealCategory);
                    break;
                default:
                    handleDefaultMacros(mealCategory); // In case of any unexpected gender value
                    break;
            }
        }
        return this;
    }

    // Handle female-specific macronutrient breakdowns
    private void handleFemaleMacros(String mealCategory) {
        switch (mealCategory != null ? mealCategory : "") {
            case "WeightLoss":
                promptBuilder.append("Ensure macronutrient breakdown falls between: Protein: 25–30%, Carbohydrates: 30–40%, Fat: 30–35%.\n");
                break;
            case "MuscleGain":
                promptBuilder.append("Ensure macronutrient breakdown falls between: Protein: 25–30%, Carbohydrates: 45–55%, Fat: 25–30%.\n");
                break;
            case "Maintenance":
                promptBuilder.append("Ensure macronutrient breakdown falls between: Protein: 20–25%, Carbohydrates: 45–50%, Fat: 30–35%.\n");
                break;
            default:
                promptBuilder.append("Assign macronutrient breakdown based on an appropriate category (Weight Loss, Muscle Gain, or Maintenance).\n");
                break;
        }
    }

    // Handle male-specific macronutrient breakdowns
    private void handleMaleMacros(String mealCategory) {
        switch (mealCategory != null ? mealCategory : "") {
            case "WeightLoss":
                promptBuilder.append("Ensure macronutrient breakdown falls between: Protein: 30–35%, Carbohydrates: 25–35%, Fat: 30–35%.\n");
                break;
            case "MuscleGain":
                promptBuilder.append("Ensure macronutrient breakdown falls between: Protein: 30–35%, Carbohydrates: 50–60%, Fat: 20–25%.\n");
                break;
            case "Maintenance":
                promptBuilder.append("Ensure macronutrient breakdown falls between: Protein: 25–30%, Carbohydrates: 50–55%, Fat: 25–30%.\n");
                break;
            default:
                promptBuilder.append("Assign macronutrient breakdown based on an appropriate category (Weight Loss, Muscle Gain, or Maintenance).\n");
                break;
        }
    }

    // Handle default macronutrient breakdowns when no gender is specified
    private void handleDefaultMacros(String mealCategory) {
        switch (mealCategory != null ? mealCategory : "") {
            case "WeightLoss":
                promptBuilder.append("Ensure macronutrient breakdown falls between: Protein: 27–32%, Carbohydrates: 28–38%, Fat: 30–35%.\n");
                break;
            case "MuscleGain":
                promptBuilder.append("Ensure macronutrient breakdown falls between: Protein: 27–32%, Carbohydrates: 48–58%, Fat: 20–28%.\n");
                break;
            case "Maintenance":
                promptBuilder.append("Ensure macronutrient breakdown falls between: Protein: 22–27%, Carbohydrates: 48–53%, Fat: 25–32%.\n");
                break;
            default:
                promptBuilder.append("Assign macronutrient breakdown based on one of the following categories: Weight Loss, Muscle Gain, or Maintenance.\n");
                break;
        }
    }

    // Add instruction to format the output as JSON, including the meal category
    public OpenAIPromptBuilder withJSONFormat() {
        promptBuilder.append("Please format the output in the following JSON structure:\n")
                .append("{\n")
                .append("  \"recipeName\": \"<recipe_name>\",\n")
                .append("  \"category\": \"<category>\",\n")  // Ensure category is included (Weight Loss, Muscle Gain, or Maintenance)
                .append("  \"ingredients\": [\"<ingredient1>\", \"<ingredient2>\", ...],\n")
                .append("  \"steps\": [\"<step1>\", \"<step2>\", ...],\n")
                .append("  \"nutritionalValues\": {\n")
                .append("    \"protein_percentage\": \"<percentage>\",\n")
                .append("    \"carbohydrates_percentage\": \"<percentage>\",\n")
                .append("    \"fat_percentage\": \"<percentage>\",\n")
                .append("    \"calories\": \"<calories>\"\n")
                .append("  }\n")
                .append("}");
        return this;
    }

    public String build() {
        return promptBuilder.toString();
    }
}
