package com.example.android.bakingapp.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.example.android.bakingapp.entities.Recipe;

import java.util.List;

public class RecipesViewModel extends AndroidViewModel {

    private RecipesRepository mRepository;

    private LiveData<List<Recipe>> mAllRecipes;

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
}
