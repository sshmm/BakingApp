package com.example.android.bakingapp.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.example.android.bakingapp.entities.Step;

import java.util.List;

public class StepsViewModel extends AndroidViewModel {

    private StepsRepository mRepository;

    private LiveData<List<Step>> mSteps;

    public StepsViewModel(Application application, int recipeId) {
        super(application);
        mRepository = new StepsRepository(application, recipeId);
        mSteps = mRepository.loadSteps();
    }

    LiveData<List<Step>> getSteps() {
        return mSteps;
    }

    public void addStep(Step step) {
        mRepository.insertStep(step);
    }
}
