package com.example.android.bakingapp.utils;

import com.example.android.bakingapp.entities.Ingredient;
import com.example.android.bakingapp.entities.Recipe;
import com.example.android.bakingapp.entities.Step;

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
    public static List<String> getListJson(String JstringArray) {
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


    public static List<Recipe> parseRecipeJson(List<String> recipes) {
        List<Recipe> recipeList = new ArrayList<>();
        if (recipes != null) {
            try {
                for (String recipe : recipes) {
                    JSONObject jRecipe = new JSONObject(recipe);
                    int id = jRecipe.getInt("id");
                    String name = jRecipe.getString("name");

                    int servings = jRecipe.getInt("servings");
                    String imageUrl = jRecipe.getString("image");

                    recipeList.add(new Recipe(id, name, servings, imageUrl));
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

    public static List<Ingredient> ingredientsFromJson(String recipe, int recipeId) {
        if (recipe != null) {
            try {
                JSONObject jRecipe = new JSONObject(recipe);
                JSONArray ingredientsArray = jRecipe.getJSONArray("ingredients");
                List<Ingredient> ingredients = parseIngredientList(recipeId, ingredientsArray);
                return ingredients;

            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }

    }

    public static List<Step> stepsFromJson(String recipe, int recipeId) {
        if (recipe != null) {
            try {
                JSONObject jRecipe = new JSONObject(recipe);
                JSONArray ingredientsArray = jRecipe.getJSONArray("steps");
                List<Step> steps = parseStepList(recipeId, ingredientsArray);
                return steps;

            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }

    }


    private static List<Ingredient> parseIngredientList(int recipeId, JSONArray ingredients) {
        List<Ingredient> ingredientsList = new ArrayList<>();
        int i = 0;
        if (ingredients != null) {
            List<String> ingredientsString = getListJson(ingredients.toString());
            try {
                for (String ingredientString : ingredientsString) {
                    JSONObject jIngredient = new JSONObject(ingredientString);
                    int quantity = jIngredient.getInt("quantity");
                    String measure = jIngredient.getString("measure");
                    String ingredient = jIngredient.getString("ingredient");
                    ingredientsList.add(new Ingredient((i + (100 * recipeId)), quantity, measure, ingredient, recipeId));
                    i++;
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


    private static List<Step> parseStepList(int recipeId, JSONArray steps) {
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
                    if (videoURL.equals("")){
                        String ext = getExt(thumbnailURL);
                        if(ext != null && ext.equals("mp4")){
                            videoURL = thumbnailURL;
                            thumbnailURL = "";
                        }
                    }


                    stepsList.add(new Step(id +(100* recipeId), shortDescription, description, videoURL, thumbnailURL, recipeId));
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

    private static String getExt(String filePath){
        int strLength = filePath.lastIndexOf(".");
        if(strLength > 0)
            return filePath.substring(strLength + 1).toLowerCase();
        return null;
    }

}
