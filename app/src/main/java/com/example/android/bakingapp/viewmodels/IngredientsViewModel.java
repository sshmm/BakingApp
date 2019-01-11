package com.example.android.bakingapp.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.example.android.bakingapp.entities.Ingredient;

import java.util.List;

public class IngredientsViewModel extends AndroidViewModel {

    private IngredientsRepository mRepository;

    private LiveData<List<Ingredient>> mIngredients;

    public IngredientsViewModel(Application application, int recipeId) {
        super(application);
        mRepository = new IngredientsRepository(application, recipeId);
        mIngredients = mRepository.loadIngredients();
    }

    LiveData<List<Ingredient>> getIngredients() {
        return mIngredients;
    }

    public void addIngredient(Ingredient ingredient) {
        mRepository.insertIngredient(ingredient);
    }
}
