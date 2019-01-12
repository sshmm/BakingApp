package com.example.android.bakingapp.databaseUtils;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.android.bakingapp.entities.Recipe;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.IGNORE;

@Dao
public interface RecipesDao {

    @Query("SELECT * FROM recipes ORDER BY id")
    LiveData<List<Recipe>> loadAllrecipes();

    @Query("SELECT * FROM recipes WHERE id = :id LIMIT 1")
    LiveData<Recipe> findRecipeById(int id);


    @Insert(onConflict = IGNORE)
    void addRecipe(Recipe recipe);

    @Delete
    void deleteRecipe(Recipe recipe);
}
