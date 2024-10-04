# mealgen
API that allows users to input ingredients they currently have available in their fridge and generate meal suggestions based on these ingredients. The user can choose between two options:
Generate a meal using only the selected ingredients.
Generate a meal that may include additional ingredients, which the user would need to purchase.

A checkbox could be provided for users to indicate whether they want to use only the selected ingredients or allow additional ingredients.
After entering the ingredients (comma-separated), the user can generate a meal, which will return:
    Recipe(s) for the meal.
    Estimated macronutrients (protein, carbohydrates, fat).
    Calorie information.

Optional Features:
Macronutrient Customization: Users can optionally specify their desired macronutrient percentages or select from predefined goal categories:
Weight Loss: Higher protein, lower carbs.
Protein: 30–35%, Carbohydrates: 25–40%, Fat: 30–35%
Muscle Gain: Higher protein and carbohydrates.
Protein: 25–35%, Carbohydrates: 40–50%, Fat: 20–30%
Maintenance: Balanced macronutrient ratio.
Protein: 25–30%, Carbohydrates: 45–50%, Fat: 25–30%
Workflow:
User inputs ingredients from their fridge.
User selects whether to generate the meal with only these ingredients or allow additional ingredients.
(Optional) User can either:
Select a meal goal (weight loss, muscle gain, or maintenance).
Enter custom macronutrient percentages
Upon clicking "Generate Meal," the API generates a meal plan that includes:
        Recipe(s).
        Macronutrient breakdown (protein, carbohydrates, fat).
        Total calories.
        Meal category (weight loss, muscle gain, maintenance, or custom).
The meal will be generated using OpenAI API. The prompt that will be given along with all the details captured will be: 


Prompt:

"Given the following input:
Ingredients (comma-separated, exclude any unidentifiable ingredients): [ingredients]
Use only the provided ingredients: [True/False]
Target macronutrient percentages (optional): [True/False]: Protein: [x]%, Carbohydrates: [y]%, Fat: [z]%
Selected goal (optional, if percentages are not specified): [True/False]:  [Weight Loss / Muscle Gain / Maintenance]
Generate a meal suggestion with a recipe using the specified ingredients and/or any additional ingredients if allowed. Also, provide the estimated macronutrient breakdown (protein, carbohydrates, fat) and total calories for the meal."


