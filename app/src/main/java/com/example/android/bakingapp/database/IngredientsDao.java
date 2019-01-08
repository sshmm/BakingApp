package com.example.android.bakingapp.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.android.bakingapp.model.Ingredient;

import java.util.List;

@Dao
public interface IngredientsDao {
    @Query("SELECT * FROM ingredients WHERE recipeId=:recipeId")
    List<Ingredient> findIngredientsForRecipe(final int recipeId);

    @Insert
    void addIngredient(Ingredient ingredient);

    @Delete
    void deleteIngredient(Ingredient ingredient);
}
