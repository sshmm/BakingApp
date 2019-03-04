package com.example.android.bakingapp;

import android.appwidget.AppWidgetManager;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.bakingapp.Widget.RecipeWidget;
import com.example.android.bakingapp.entities.IdType;
import com.example.android.bakingapp.entities.Ingredient;
import com.example.android.bakingapp.entities.Step;
import com.example.android.bakingapp.viewmodels.RecipesViewModel;

import java.util.List;

/**
 * An activity representing a list of Recipes. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link RecipeDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class RecipeListActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "position";
    private static final int DEFAULT_POSITION = -1;
    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private static final String TAG = RecipeListActivity.class.getSimpleName();
    private boolean mTwoPane;
    private List<Step> mSteps;
    private List<Ingredient> mIngredients;
    private RecipesViewModel recipesViewModel;
    private SimpleItemRecyclerViewAdapter adapter;
    private RecyclerView recyclerView;
    private Parcelable savedRecyclerLayoutState;
    private static final String BUNDLE_RECYCLER_LAYOUT = "recycler_layout";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);


        recipesViewModel = ViewModelProviders.of(this).get(RecipesViewModel.class);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        if (findViewById(R.id.recipe_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        recyclerView = findViewById(R.id.recipe_list);
        assert recyclerView != null;

        Intent intent = getIntent();
        if (intent == null) {
            Log.e("Up", "No intent");

            closeOnError();
        }

        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            //EXTRA_POSITION not found in intent
            Log.e("Up", "No Default POs");
            closeOnError();
            return;
        }

        adapter = new SimpleItemRecyclerViewAdapter(this, mTwoPane);
        recipesViewModel.getSteps(position + 1).observe(this, new Observer<List<Step>>() {
            @Override
            public void onChanged(@Nullable List<Step> steps) {
                adapter.setStepsData(steps);
            }
        });


        adapter.setmIngredient(position);
        setupRecyclerView((RecyclerView) recyclerView);

        Intent intentBroad = new Intent(this, RecipeWidget.class);
        intentBroad.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
// Use an array and EXTRA_APPWIDGET_IDS instead of AppWidgetManager.EXTRA_APPWIDGET_ID,
// since it seems the onUpdate() is only fired on that:
        AppWidgetManager widgetManager = AppWidgetManager.getInstance(this);
        int[] ids = widgetManager.getAppWidgetIds(new ComponentName(this, RecipeWidget.class));
         widgetManager.notifyAppWidgetViewDataChanged(ids, android.R.id.list);

        intentBroad.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        intentBroad.putExtra("recipe",position);
        sendBroadcast(intentBroad);

    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(adapter);

        if (savedRecyclerLayoutState != null){
            recyclerView.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);
        }
    }

    public void closeOnError() {
        finish();
        Toast.makeText(this, "Recipe data is not available.", Toast.LENGTH_LONG).show();
    }

    private static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final RecipeListActivity mParentActivity;
        private List<Step> mSteps;
        private Integer mPosition;
        private final boolean mTwoPane;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IdType idType = (IdType) view.getTag();
                if (mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putInt(RecipeDetailFragment.REC_ID, idType.getRecipeId());
                    arguments.putInt(RecipeDetailFragment.STEP_ID, idType.getStepId());

                    if (idType.getType().equals("ingredient")){
                        RecipeDetailFragment fragment = new RecipeDetailFragment();
                        fragment.setArguments(arguments);
                        mParentActivity.getSupportFragmentManager().beginTransaction()
                                .replace(R.id.recipe_detail_container, fragment)
                                .commit();
                    } else {
                        StepsFragment fragment = new StepsFragment();
                        fragment.setArguments(arguments);
                        mParentActivity.getSupportFragmentManager().beginTransaction()
                                .replace(R.id.recipe_detail_container, fragment)
                                .commit();
                    }

                } else {
                    Context context = view.getContext();
                    Intent intent;
                    if (idType.getType().equals("ingredient")){
                        intent = new Intent(context, RecipeDetailActivity.class);
                        intent.putExtra(RecipeDetailFragment.REC_ID, idType.getRecipeId());
                        intent.putExtra(RecipeDetailFragment.STEP_ID, idType.getStepId());
                    } else{
                        intent = new Intent(context, RecipeStepsActivity.class);
                        intent.putExtra(StepsFragment.REC_ID, idType.getRecipeId());
                        intent.putExtra(StepsFragment.STEP_ID, idType.getStepId());
                    }


                    context.startActivity(intent);
                }
            }
        };

        SimpleItemRecyclerViewAdapter(RecipeListActivity parent,
                                      boolean twoPane) {
            mParentActivity = parent;
            mTwoPane = twoPane;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recipe_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            /*List<Ingredient> ingredientList
                    = mValues.getIngredients();*/
            if (position > 0) {
                position = position - 1;
                holder.mContentView.setText(mSteps.get(position).getShortDescription());
                holder.itemView.setTag(new IdType(mPosition + 1,mSteps.get(position).getStepId(),"step"));
            } else {
                holder.mContentView.setText("Ingredients List");

                holder.itemView.setTag(new IdType(mPosition+1,mSteps.get(position).getStepId(),"ingredient"));
            }
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            if (null == mSteps) return 0;
            return (mSteps.size() + 1);
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView mContentView;

            ViewHolder(View view) {
                super(view);
                mContentView = view.findViewById(R.id.content);
            }
        }

        public void setStepsData(List<Step> steps) {
            mSteps = steps;
            notifyDataSetChanged();
        }

        public void setmIngredient(Integer position) {
            mPosition = position;
            notifyDataSetChanged();
        }


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(BUNDLE_RECYCLER_LAYOUT,
                recyclerView.getLayoutManager().onSaveInstanceState());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        //restore recycler view at same position
        if (savedInstanceState != null) {
            savedRecyclerLayoutState = savedInstanceState.getParcelable(BUNDLE_RECYCLER_LAYOUT);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (savedRecyclerLayoutState != null){
            recyclerView.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);
        }
    }
}
