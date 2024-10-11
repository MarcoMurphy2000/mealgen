Project Overview:
API that allows users to input ingredients they currently have available in their fridge and generate meals based on these ingredients. The user can choose between two options:
Generate a meal using only the entered ingredients.
Generate a meal that may include additional ingredients.
A checkbox could be provided for users to indicate whether they want to use only the selected ingredients or allow additional ingredients.
After entering the ingredients (comma-separated), if the user chooses to generate a meal with only the provided ingredients, OpenAI will generate a meal and provide the estimated macronutrients, calorie information, and the meal category. 
If the user chooses that the meal may include additional ingredients, the user must specify the meal category (weight loss, muscle gain, maintenance), and whether they are male or female. This is so that Open AI can generate a meal with the right nutritional percentages for the different sex. 

The generated meal will then include:
    Recipe for the meal.
    Estimated macronutrients (protein, carbohydrates, fat).
    Calorie information.
    Meal category (weight loss, muscle gain, maintenance)

This is to be given to OpenAI as part of the prompt to use when generating the meal:
For Women:
Weight Loss:
Protein: 25–30%
Carbohydrates: 30–40%
Fat: 30–35%
Muscle Gain:
Protein: 25–30%
Carbohydrates: 45–55%
Fat: 25–30%
Maintenance:
Protein: 20–25%
Carbohydrates: 45–50%
Fat: 30–35%
For Men:
Weight Loss:
Protein: 30–35%
Carbohydrates: 25–35%
Fat: 30–35%
Muscle Gain:
Protein: 30–35%
Carbohydrates: 50–60%
Fat: 20–25%
Maintenance:
Protein: 25–30%
Carbohydrates: 50–55%
Fat: 25–30%


Workflow:
User inputs ingredients from their fridge.
User selects whether to generate the meal with only these ingredients or allow additional ingredients.
If additional ingredients is allowed, user must specify meal category and their gender. 
Upon clicking "Generate Meal," the API generates a meal plan that includes:
        Recipe for the meal .
        Macronutrient breakdown (protein, carbohydrates, fat).
        Total calories.
        Meal category (weight loss, muscle gain, maintenance).
The meal will be generated using OpenAI API. The prompt that will be given along with all the details captured should be something like : 


Prompt:

"User provided input:
Ingredients (comma-separated, exclude any unidentifiable ingredients): [ingredients]
Use only the provided ingredients: [True/False]
If False:
Gender [Male/Female]
Selected goal [Weight Loss / Muscle Gain / Maintenance]
Given the above information, generate a meal that should be formatted in this output:
        Recipe for the meal .
        Macronutrient breakdown (protein, carbohydrates, fat).
        Total calories.
        Meal category (weight loss, muscle gain, maintenance).
Use this for generating the meal if the user specified their gender and a meal category after choosing to include additional ingredients: Also use this to calculate the meal category if the user did not specify any.
For Women:
Weight Loss:
Protein: 25–30%
Carbohydrates: 30–40%
Fat: 30–35%
Muscle Gain:
Protein: 25–30%
Carbohydrates: 45–55%
Fat: 25–30%
Maintenance:
Protein: 20–25%
Carbohydrates: 45–50%
Fat: 30–35%
For Men:
Weight Loss:
Protein: 30–35%
Carbohydrates: 25–35%
Fat: 30–35%
Muscle Gain:
Protein: 30–35%
Carbohydrates: 50–60%
Fat: 20–25%
Maintenance:
Protein: 25–30%
Carbohydrates: 50–55%
Fat: 25–30%
"

