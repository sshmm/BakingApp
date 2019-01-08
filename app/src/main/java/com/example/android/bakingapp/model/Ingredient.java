package com.example.android.bakingapp.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import static android.arch.persistence.room.ForeignKey.CASCADE;


@Entity(tableName = "ingredients", foreignKeys = @ForeignKey(entity = Recipe.class,
        parentColumns = "id",
        childColumns = "recipeId",
        onDelete = CASCADE))
public class Ingredient {
    @PrimaryKey
    private int ingredientId;
    private int quantity;
    private String measure;
    private String ingredient;
    private int recipeId;


    public Ingredient(int ingredientId, int quantity, String measure, String ingredient, int recipeId) {
        this.ingredientId = ingredientId;
        this.quantity = quantity;
        this.measure = measure;
        this.ingredient = ingredient;
        this.recipeId = recipeId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    public int getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(int ingredientId) {
        this.ingredientId = ingredientId;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }
}
