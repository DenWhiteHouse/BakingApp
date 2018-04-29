package com.example.android.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.example.android.bakingapp.data.Recipe;

import java.util.ArrayList;

import static com.example.android.bakingapp.RecipeDetailsActivity.RECIPE_DETAIL;


public class MainActivity extends AppCompatActivity implements RecipeAdapter.ListItemClickListener {

    //Bundle Variables for coordinating Fragments Intents and Bundles
    public static String FETCHEDRECIPES = "FETCHEDRECIPE";
    public static String RECIPE = "RECIPE";
    public static String STEP = "STEP";
    public static String INDEX = "INDEX";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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







