package com.example.android.bakingapp.utils;

import com.example.android.bakingapp.model.Ingredient;
import com.example.android.bakingapp.model.Recipe;
import com.example.android.bakingapp.model.Step;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {
    /**
     * Extracts the List of Strings from the JSON Array
     *
     * @param JstringArray the JSON Array as String
     * @return List of JSON objects ass Strings
     */
    private static List<String> getListJson(String JstringArray) {
        try {

            JSONArray JsonArray = new JSONArray(JstringArray);
            List<String> jObjects = new ArrayList<String>();
            for (int i = 0; i < JsonArray.length(); i++) {
                jObjects.add(JsonArray.getString(i));
            }

            return jObjects;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }


    public static List<Recipe> parseRecipeJson(String JstringArray) {
        List<String> recipes = getListJson(JstringArray);
        List<Recipe> recipeList = new ArrayList<>();
        if (recipes != null) {
            try {
                for (String recipe : recipes) {
                    JSONObject jRecipe = new JSONObject(recipe);
                    int id = jRecipe.getInt("id");
                    String name = jRecipe.getString("name");

                    JSONArray ingredientsArray = jRecipe.getJSONArray("ingredients");
                    List<Ingredient> ingredients = parseIngredientList(ingredientsArray);

                    JSONArray stepsArray = jRecipe.getJSONArray("steps");
                    List<Step> steps = parseStepList(stepsArray);

                    int servings = jRecipe.getInt("servings");
                    String imageUrl = jRecipe.getString("image");

                    recipeList.add(new Recipe(id, name, ingredients, steps, servings, imageUrl));
                }
                return recipeList;

            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }

    }

    private static List<Ingredient> parseIngredientList(JSONArray ingredients) {
        List<Ingredient> ingredientsList = new ArrayList<>();
        if (ingredients != null) {
            List<String> ingredientsString = getListJson(ingredients.toString());
            try {
                for (String ingredientString : ingredientsString) {
                    JSONObject jIngredient = new JSONObject(ingredientString);
                    int quantity = jIngredient.getInt("quantity");
                    String measure = jIngredient.getString("measure");
                    String ingredient = jIngredient.getString("ingredient");
                    ingredientsList.add(new Ingredient(quantity, measure, ingredient));
                }
                return ingredientsList;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }


    private static List<Step> parseStepList(JSONArray steps) {
        List<Step> stepsList = new ArrayList<>();
        if (steps != null) {
            List<String> stepsString = getListJson(steps.toString());
            try {
                for (String stepString : stepsString) {
                    JSONObject jStep = new JSONObject(stepString);
                    int id = jStep.getInt("id");
                    String shortDescription = jStep.getString("shortDescription");
                    String description = jStep.getString("description");
                    String videoURL = jStep.getString("videoURL");
                    String thumbnailURL = jStep.getString("thumbnailURL");
                    stepsList.add(new Step(id, shortDescription, description, videoURL, thumbnailURL));
                }
                return stepsList;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

}
