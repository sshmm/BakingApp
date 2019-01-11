package com.example.android.bakingapp.databaseUtils;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.android.bakingapp.entities.Ingredient;
import com.example.android.bakingapp.entities.Recipe;
import com.example.android.bakingapp.entities.Step;

@Database(entities = {Recipe.class, Ingredient.class, Step.class}, version = 1, exportSchema = false)
public abstract class RecipesDatabase extends RoomDatabase {

    private static final String DB_NAME = "recipesDatabase.db";
    private static final Object LOCK = new Object();
    private static volatile RecipesDatabase instance;

    public static synchronized RecipesDatabase getInstance(Context context) {
        if (instance == null) {
            instance = create(context);
        }
        return instance;
    }

    private static RecipesDatabase create(final Context context) {
        synchronized (LOCK) {
            return Room.databaseBuilder(
                    context,
                    RecipesDatabase.class,
                    DB_NAME).build();
        }
    }

    public abstract RecipesDao recipesDao();

    public abstract IngredientsDao ingredientsDao();

    public abstract StepsDao stepsDao();

}