package com.example.android.bakingapp;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.android.bakingapp.adapters.RecipesAdapter;
import com.example.android.bakingapp.databaseUtils.DatabaseExecuter;
import com.example.android.bakingapp.entities.Ingredient;
import com.example.android.bakingapp.entities.Recipe;
import com.example.android.bakingapp.entities.Step;
import com.example.android.bakingapp.utils.JsonUtils;
import com.example.android.bakingapp.utils.NetworkUtils;
import com.example.android.bakingapp.viewmodels.RecipesViewModel;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.android.bakingapp.utils.NetworkUtils.buildURL;

public class MainActivity extends AppCompatActivity implements RecipesAdapter.RecipesAdapterOnClickHandler, LoaderManager.LoaderCallbacks<String> {
    // private LinearLayoutManager layoutManagerPhone;
    private static final int RECIPES_LOADER = 33;
    @BindView(R.id.tv_connection_error)
    TextView connectionErrorView;
    private RecipesAdapter recipesAdapter;
    private RecyclerView recyclerView;
    private GridLayoutManager layoutManager;
    private RecipesViewModel recipesViewModel;

    private Parcelable savedRecyclerLayoutState;
    private static final String BUNDLE_RECYCLER_LAYOUT = "recycler_layout";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        recipesAdapter = new RecipesAdapter(this, MainActivity.this);

        recipesViewModel = ViewModelProviders.of(this).get(RecipesViewModel.class);

        if (findViewById(R.id.recycler_view_phone) != null) {
            recyclerView = findViewById(R.id.recycler_view_phone);
            layoutManager
                    = new GridLayoutManager(this, 1);
        }

        if (findViewById(R.id.recycler_view_tablet) != null) {
            recyclerView = findViewById(R.id.recycler_view_tablet);
            layoutManager
                    = new GridLayoutManager(this, 2);
        }

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(recipesAdapter);

        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<String> recipesLoader = loaderManager.getLoader(RECIPES_LOADER);
        Bundle queryBundle = new Bundle();

        if (recipesLoader == null) {
            loaderManager.initLoader(RECIPES_LOADER, queryBundle, this);

        } else {
            loaderManager.restartLoader(RECIPES_LOADER, queryBundle, this);
        }
        recipesViewModel.getAllRecipes().observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(@Nullable List<Recipe> recipes) {
                recipesAdapter.setRecipeData(recipes);

            }
        });

    }

    @Override
    public void onClick(int position) {
        Intent intent = new Intent(this, RecipeListActivity.class);
        intent.putExtra(RecipeListActivity.EXTRA_POSITION, position);
        startActivity(intent);
    }

    @Override
    public Loader<String> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<String>(this) {
            String recipesJson;

            @Override
            protected void onStartLoading() {
                if (recipesJson != null) {
                    deliverResult(recipesJson);
                } else {
                    forceLoad();
                }

            }

            @Override
            public void deliverResult(String data) {
                recipesJson = data;
                super.deliverResult(data);
            }

            @Override
            public String loadInBackground() {
                try {
                    return NetworkUtils.getResponseFromHttpUrl(buildURL());
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }

            }
        };
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        List<String> recipesJsonList;
        final List<Recipe> recipesListResults;
        List<Step> stepsListResults;
        List<Ingredient> ingredientsListResults;


        if (data != null && !data.equals("")) {
            recipesJsonList = JsonUtils.getListJson(data);
            recipesListResults = JsonUtils.parseRecipeJson(recipesJsonList);
            recipesAdapter.setRecipeData(recipesListResults);
            for (int i = 0; i < recipesListResults.size(); i++) {
                stepsListResults = JsonUtils.stepsFromJson(recipesJsonList.get(i), i + 1);
                ingredientsListResults = JsonUtils.ingredientsFromJson(recipesJsonList.get(i), i + 1);
                final int finalI = i;
                DatabaseExecuter.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        recipesViewModel.addRecipe(recipesListResults.get(finalI));
                    }
                });

                for (final Ingredient ingredient : ingredientsListResults) {
                    DatabaseExecuter.getInstance().diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            recipesViewModel.addIngredient(ingredient);
                        }
                    });
                }

                for (final Step step : stepsListResults) {
                    DatabaseExecuter.getInstance().diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            recipesViewModel.addStep(step);
                        }
                    });
                }

            }
            recipesAdapter.setRecipeData(recipesListResults);

            recyclerView.setVisibility(View.VISIBLE);
            layoutManager.onRestoreInstanceState(savedRecyclerLayoutState);
        }
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(BUNDLE_RECYCLER_LAYOUT,
                layoutManager.onSaveInstanceState());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        //restore recycler view at same position
        if (savedInstanceState != null) {
            savedRecyclerLayoutState = savedInstanceState.getParcelable(BUNDLE_RECYCLER_LAYOUT);
        }
    }
}
