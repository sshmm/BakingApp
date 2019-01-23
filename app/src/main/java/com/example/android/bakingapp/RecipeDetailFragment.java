package com.example.android.bakingapp;

import android.app.Activity;
import android.app.NotificationManager;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaButtonReceiver;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingapp.dummy.DummyContent;
import com.example.android.bakingapp.entities.Ingredient;
import com.example.android.bakingapp.entities.Step;
import com.example.android.bakingapp.viewmodels.RecipesViewModel;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.List;

/**
 * A fragment representing a single Recipe detail screen.
 * This fragment is either contained in a {@link RecipeListActivity}
 * in two-pane mode (on tablets) or a {@link RecipeDetailActivity}
 * on handsets.
 */
public class RecipeDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String REC_ID = "rec_id";
    public static final String STEP_ID = "step_id";

    private RecipesViewModel recipesViewModel;
    private SimpleItemRecyclerViewAdapter adapter;
    private static final String TAG = RecipeDetailFragment.class.getSimpleName();


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RecipeDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments().containsKey(REC_ID)) {

            recipesViewModel = ViewModelProviders.of(getActivity()).get(RecipesViewModel.class);

            recipesViewModel.getIngredients(getArguments().getInt(REC_ID)).observe(this, new Observer<List<Ingredient>>() {
                @Override
                public void onChanged(@Nullable List<Ingredient> ingredients) {
                    Log.e("ViewModel",ingredients.get(getArguments().getInt(REC_ID)).getIngredient());
                    adapter.setIngredientsData(ingredients);
                }
            });


            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(/*mItem.content*/"");
            }






        }
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recipe_detail, container, false);
        final View recyclerView = rootView.findViewById(R.id.ingredient_list);



        assert recyclerView != null;
        adapter = new SimpleItemRecyclerViewAdapter();

        setupRecyclerView((RecyclerView) recyclerView);
        // Show the dummy content as text in a TextView.
        if (""/*mItem*/ != null) {
            ((TextView) rootView.findViewById(R.id.recipe_detail)).setText(/*mItem.details*/"details");
        }

        return rootView;
    }


    private static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private List<Ingredient> mIngredient;
        SimpleItemRecyclerViewAdapter() {

        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recipe_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
                holder.mContentView.setText(mIngredient.get(position).getIngredient());
                Log.e("Ingredient Id: ",String.valueOf(mIngredient.get(position).getIngredientId()));
                }

        @Override
        public int getItemCount() {
            if (null == mIngredient) return 0;
            return (mIngredient.size());
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView mContentView;

            ViewHolder(View view) {
                super(view);
                mContentView = view.findViewById(R.id.content);
            }
        }

        public void setIngredientsData(List<Ingredient> ingredients) {
            mIngredient = ingredients;
            notifyDataSetChanged();
        }

    }



}
