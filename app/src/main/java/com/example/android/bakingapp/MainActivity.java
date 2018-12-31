package com.example.android.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.android.bakingapp.adapters.RecipesAdapter;
import com.example.android.bakingapp.model.Recipe;
import com.example.android.bakingapp.utils.JsonUtils;
import com.example.android.bakingapp.utils.NetworkUtils;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        recipesAdapter = new RecipesAdapter(this, MainActivity.this);


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
        List<Recipe> recipesListResults;


        if (data != null && !data.equals("")) {
            recipesListResults = JsonUtils.parseRecipeJson(data);
            recipesAdapter.setRecipeData(recipesListResults);
            recyclerView.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }
}
