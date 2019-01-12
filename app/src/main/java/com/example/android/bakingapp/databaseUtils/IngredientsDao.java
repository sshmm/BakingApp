package com.example.android.bakingapp.databaseUtils;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.android.bakingapp.entities.Ingredient;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.IGNORE;

@Dao
public interface IngredientsDao {
    @Query("SELECT * FROM ingredients WHERE recipeId=:recipeId ORDER BY ingredientId ASC")
    LiveData<List<Ingredient>> findIngredientsForRecipe(final int recipeId);

    @Insert(onConflict = IGNORE)
    void addIngredient(Ingredient ingredient);

    @Delete
    void deleteIngredient(Ingredient ingredient);
}
