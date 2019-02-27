package com.example.android.bakingapp;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.example.android.bakingapp.entities.Step;
import com.example.android.bakingapp.viewmodels.RecipesViewModel;

public class RecipeStepsActivity extends AppCompatActivity {
    private RecipesViewModel recipesViewModel;
    private Step mStep;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

/*        recipesViewModel = ViewModelProviders.of(this).get(RecipesViewModel.class);
        recipesViewModel.getStep(getIntent().getIntExtra(StepsFragment.STEP_ID,0)).observe(this, new Observer<Step>() {
            @Override
            public void onChanged(@Nullable Step step) {
                mStep = step;
                Log.e("ddddddddddd", step.getVideoURL());
            }
        });
*/
        setContentView(R.layout.activity_recipe_steps);
        Toolbar toolbar = findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);


        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putInt(StepsFragment.STEP_ID,
                    getIntent().getIntExtra(StepsFragment.STEP_ID,0));
            arguments.putInt(StepsFragment.REC_ID,
                    getIntent().getIntExtra(StepsFragment.REC_ID,1));
            StepsFragment fragment = new StepsFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.recipe_detail_container, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            Intent intent= new Intent(this, RecipeListActivity.class);
            intent.putExtra(RecipeListActivity.EXTRA_POSITION,getIntent().getIntExtra(RecipeDetailFragment.REC_ID,0) -1);
            navigateUpTo(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}


