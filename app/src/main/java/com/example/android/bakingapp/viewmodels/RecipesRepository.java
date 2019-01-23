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
    private LiveData<List<Recipe>> mRecipes;
    private StepsDao stepsDao;
    private LiveData<List<Step>> mSteps;
    private LiveData<Step> mStep;
    private IngredientsDao ingredientsDao;
    private LiveData<List<Ingredient>> mIngredients;

    RecipesRepository(Application application) {
        RecipesDatabase db = RecipesDatabase.getInstance(application);
        recipesDao = db.recipesDao();
        mRecipes = recipesDao.loadAllrecipes();

        stepsDao = db.stepsDao();

        ingredientsDao = db.ingredientsDao();
    }


    public LiveData<List<Recipe>> loadAllrecipes() {
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

    public LiveData<List<Step>> loadSteps(int recipeId) {
        mSteps = stepsDao.findStepsForRecipe(recipeId);
        return mSteps;
    }

    public LiveData<Step> loadStep(int stepId){
        mStep = stepsDao.findStep(stepId);
        return mStep;
    }

    public void insertStep(final Step step) {
        DatabaseExecuter.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                stepsDao.addStep(step);
            }
        });
    }

    public LiveData<List<Ingredient>> loadIngredients(int recipeId) {
        mIngredients = ingredientsDao.findIngredientsForRecipe(recipeId);
        return mIngredients;
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
