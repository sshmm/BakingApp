package com.example.android.bakingapp.Widget;

import java.util.ArrayList;
import java.util.List;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService.RemoteViewsFactory;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.RecipeDetailFragment;
import com.example.android.bakingapp.databaseUtils.DatabaseExecuter;
import com.example.android.bakingapp.databaseUtils.RecipesDatabase;
import com.example.android.bakingapp.entities.Ingredient;
import com.example.android.bakingapp.entities.Recipe;

/**
 * If you are familiar with Adapter of ListView,this is the same as adapter
 * with few changes
 *
 */
public class RecipeWidgetFactory implements RemoteViewsFactory {
    private Context context;
    private List<Ingredient> data;
    private int appWidgetId;
    private int recipeId;

    public RecipeWidgetFactory(Context context, Intent intent) {
        this.context = context;
        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
        recipeId = intent.getIntExtra("recipe",1);


    }


    @Override
    public int getCount() {
        if (data == null){

            return 0;
        }else {
        return data.size();}
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /*
     *Similar to getView of Adapter where instead of View
     *we return RemoteViews
     *
     */
    @Override
    public RemoteViews getViewAt(int position) {

        final RemoteViews remoteView = new RemoteViews(
                context.getPackageName(), R.layout.widget_list_row);
       if (data == null){

       }
       else{
           String ingredient = data.get(position).getQuantity() + " " +
                   data.get(position).getMeasure() + " Of " +
                   data.get(position).getIngredient();

           remoteView.setTextViewText(R.id.content,ingredient);

       }


        return remoteView;
    }


    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public void onCreate() {

        DatabaseExecuter.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                data = RecipesDatabase.getInstance(context).ingredientsDao().findIngredientsForRecipe2(recipeId + 1);
            }
        });
    }


    @Override
    public void onDataSetChanged() {





    }


    @Override
    public void onDestroy() {
    }

}