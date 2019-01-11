package com.example.android.bakingapp.viewmodels;

import android.app.Application;
import android.arch.lifecycle.LiveData;

import com.example.android.bakingapp.databaseUtils.DatabaseExecuter;
import com.example.android.bakingapp.databaseUtils.IngredientsDao;
import com.example.android.bakingapp.databaseUtils.RecipesDatabase;
import com.example.android.bakingapp.entities.Ingredient;

import java.util.List;

public class IngredientsRepository {
    private IngredientsDao ingredientsDao;
    private LiveData<List<Ingredient>> mIngredients;

    IngredientsRepository(Application application, int recipeId) {
        RecipesDatabase db = RecipesDatabase.getInstance(application);
        ingredientsDao = db.ingredientsDao();
        mIngredients = ingredientsDao.findIngredientsForRecipe(recipeId);
    }


    LiveData<List<Ingredient>> loadIngredients() {
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
