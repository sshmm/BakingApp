package com.example.android.bakingapp.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.graphics.Movie;

import com.example.android.bakingapp.model.Recipe;

import java.util.List;

@Dao
public interface RecipesDao {

    @Query("SELECT * FROM recipes ORDER BY id")
    LiveData<List<Recipe>> loadAllrecipes();

    @Query("SELECT * FROM recipes WHERE id = :id LIMIT 1")
    LiveData<Movie> findRecipeById(int id);


    @Insert
    void addRecipe(Recipe recipe);

    @Delete
    void deleteRecipe(Recipe recipe);
}
