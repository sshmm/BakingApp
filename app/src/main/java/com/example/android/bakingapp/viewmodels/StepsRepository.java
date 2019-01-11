package com.example.android.bakingapp.viewmodels;

import android.app.Application;
import android.arch.lifecycle.LiveData;

import com.example.android.bakingapp.databaseUtils.DatabaseExecuter;
import com.example.android.bakingapp.databaseUtils.RecipesDatabase;
import com.example.android.bakingapp.databaseUtils.StepsDao;
import com.example.android.bakingapp.entities.Step;

import java.util.List;

public class StepsRepository {
    private StepsDao stepsDao;
    private LiveData<List<Step>> mSteps;

    StepsRepository(Application application, int recipeId) {
        RecipesDatabase db = RecipesDatabase.getInstance(application);
        stepsDao = db.stepsDao();
        mSteps = stepsDao.findStepsForRecipe(recipeId);
    }


    LiveData<List<Step>> loadSteps() {
        return mSteps;
    }

    public void insertStep(final Step step) {
        DatabaseExecuter.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                stepsDao.addStep(step);
            }
        });
    }


}
