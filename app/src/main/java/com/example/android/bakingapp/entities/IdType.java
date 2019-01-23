package com.example.android.bakingapp.entities;

public class IdType   {
    private int recipeId;
    private int stepId;
    private String type;
    public IdType(int recipeId, int stepId, String type){
        this.recipeId = recipeId;
        this.stepId = stepId;
        this.type = type;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public int getStepId() {
        return stepId;
    }

    public String getType() {
        return type;
    }
}
