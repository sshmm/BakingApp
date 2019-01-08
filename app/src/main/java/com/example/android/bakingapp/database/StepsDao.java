package com.example.android.bakingapp.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.android.bakingapp.model.Step;

import java.util.List;

@Dao
public interface StepsDao {

    @Query("SELECT * FROM steps WHERE recipeId=:recipeId")
    List<Step> findStepsForRecipe(final int recipeId);

    @Insert
    void addStep(Step step);

    @Delete
    void deleteStep(Step step);
}
