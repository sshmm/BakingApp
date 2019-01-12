package com.example.android.bakingapp.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.example.android.bakingapp.entities.Ingredient;
import com.example.android.bakingapp.entities.Recipe;
import com.example.android.bakingapp.entities.Step;

import java.util.List;

public class RecipesViewModel extends AndroidViewModel {

    private RecipesRepository mRepository;

    private LiveData<List<Recipe>> mAllRecipes;
    private LiveData<List<Ingredient>> mIngredients;
    private LiveData<List<Step>> mSteps;

    public RecipesViewModel(Application application) {
        super(application);
        mRepository = new RecipesRepository(application);
        mAllRecipes = mRepository.loadAllrecipes();
    }

    public LiveData<List<Recipe>> getAllRecipes() {
        return mAllRecipes;
    }

    public void addRecipe(Recipe recipe) {
        mRepository.insertRecipe(recipe);
    }

    public LiveData<List<Step>> getSteps(int recipeId) {
        mSteps = mRepository.loadSteps(recipeId);
        return mSteps;
    }

    public void addStep(Step step) {
        mRepository.insertStep(step);
    }

    public LiveData<List<Ingredient>> getIngredients(int recipeId) {
        mIngredients = mRepository.loadIngredients(recipeId);
        return mIngredients;
    }

    public void addIngredient(Ingredient ingredient) {
        mRepository.insertIngredient(ingredient);
    }
}

