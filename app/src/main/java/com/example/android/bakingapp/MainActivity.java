package com.example.android.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;

import com.example.android.bakingapp.data.Recipe;
import com.example.android.bakingapp.espresso.SimpleIdlingResource;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements RecipeAdapter.ListItemClickListener {

    //Bundle Variables for coordinating Fragments Intents and Bundles
    public static String FETCHEDRECIPES = "FETCHEDRECIPE";
    public static String RECIPE = "RECIPE";
    public static String STEP = "STEP";
    public static String INDEX = "INDEX";

    @Nullable
    private SimpleIdlingResource mIdlingResource;

    //IdleResource for Testing JSON received with Retrofit (So not in Sync) so to move forward is needed and Idling Resource
    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // get and istance of Idling Resource for testing
        getIdlingResource();
    }

    @Override
    public void onListItemClick(Recipe selectedItemIndex) {

        //Bundle to pass the JSON
        Bundle recipeBundle = new Bundle();
        ArrayList<Recipe> recipe = new ArrayList<>();
        recipe.add(selectedItemIndex);
        recipeBundle.putParcelableArrayList(RECIPE, recipe);

        //Intent for accessing the Details Activty
        final Intent intent = new Intent(this, RecipeDetailsActivity.class);
        intent.putExtras(recipeBundle);
        startActivity(intent);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

}







