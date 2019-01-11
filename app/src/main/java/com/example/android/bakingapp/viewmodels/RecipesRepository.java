package com.example.android.bakingapp.viewmodels;

import android.app.Application;
import android.arch.lifecycle.LiveData;

import com.example.android.bakingapp.databaseUtils.DatabaseExecuter;
import com.example.android.bakingapp.databaseUtils.IngredientsDao;
import com.example.android.bakingapp.databaseUtils.RecipesDao;
import com.example.android.bakingapp.databaseUtils.RecipesDatabase;
import com.example.android.bakingapp.databaseUtils.StepsDao;
import com.example.android.bakingapp.entities.Ingredient;
import com.example.android.bakingapp.entities.Recipe;
import com.example.android.bakingapp.entities.Step;

import java.util.List;

public class RecipesRepository {
    private RecipesDao recipesDao;
    private StepsDao stepsDao;
    private IngredientsDao ingredientsDao;
    private LiveData<List<Recipe>> mRecipes;

    RecipesRepository(Application application) {
        RecipesDatabase db = RecipesDatabase.getInstance(application);
        recipesDao = db.recipesDao();
        stepsDao = db.stepsDao();
        ingredientsDao = db.ingredientsDao();
        mRecipes = recipesDao.loadAllrecipes();
    }


    LiveData<List<Recipe>> loadAllrecipes() {
        return mRecipes;
    }


    public void insertRecipe(final Recipe recipe) {
        DatabaseExecuter.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                recipesDao.addRecipe(recipe);
            }
        });
    }

    public void insertStep(final Step step) {
        DatabaseExecuter.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                stepsDao.addStep(step);
            }
        });
    }

    public void insertIngredient(final Ingredient ingredient) {
        DatabaseExecuter.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                ingredientsDao.addIngredient(ingredient);
            }
        });
    }

}
