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
import com.example.android.bakingapp.entities.Recipe;

/**
 * If you are familiar with Adapter of ListView,this is the same as adapter
 * with few changes
 *
 */
public class RecipeWidgetFactory implements RemoteViewsFactory {
    private ArrayList<Recipe> listItemList = new ArrayList<Recipe>();
    private Context context;
    private List<Recipe> data;
    private int appWidgetId;

    public RecipeWidgetFactory(Context context, Intent intent) {
        this.context = context;
        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);


    }
/*
    private void populateListItem() {
        for (int i = 0; i < 10; i++) {
            Recipe recipe = new Recipe(i,String.valueOf(i)+"kkkkkkkkkkkkkkk",i,String.valueOf(i));
            listItemList.add(recipe);
        }

    }*/

    @Override
    public int getCount() {
        if (data == null){
            Log.e("Test3", String.valueOf(0));

            return 0;
        }else {
        Log.e("Test3", String.valueOf(data.size()));
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
       if (data == null)
           Log.e("Test2", "Empty");
       else{
           Log.e("Test2", data.get(2).getName());
           Log.e("Test2", String.valueOf(position));
           remoteView.setTextViewText(R.id.content,data.get(position).getName());

           Intent fillInIntent = new Intent();
           fillInIntent.putExtra(RecipeDetailFragment.REC_ID,data.get(position).getId());
           remoteView.setOnClickFillInIntent(R.id.content, fillInIntent);

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
                data = RecipesDatabase.getInstance(context).recipesDao().loadAllrecipes2();
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