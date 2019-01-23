package com.example.android.bakingapp.databaseUtils;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.android.bakingapp.entities.Step;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.IGNORE;

@Dao
public interface StepsDao {

    @Query("SELECT * FROM steps WHERE recipeId=:recipeId ORDER BY stepId ASC")
    LiveData<List<Step>> findStepsForRecipe(final int recipeId);

    @Query("SELECT * FROM steps WHERE stepId=:stepId")
    LiveData<Step> findStep(final int stepId);

    @Insert(onConflict = IGNORE)
    void addStep(Step step);

    @Delete
    void deleteStep(Step step);
}
