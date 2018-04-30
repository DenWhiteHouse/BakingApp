package com.example.android.bakingapp;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.example.android.bakingapp.data.Recipe;
import com.example.android.bakingapp.data.Step;
import com.example.android.bakingapp.fragments.DetailsFragment;
import com.example.android.bakingapp.fragments.StepsFragment;

import java.util.ArrayList;
import java.util.List;

import static com.example.android.bakingapp.MainActivity.RECIPE;
import static com.example.android.bakingapp.MainActivity.STEP;
import static com.example.android.bakingapp.MainActivity.INDEX;

/**
 * Created by casab on 27/04/2018.
 */

public class RecipeDetailsActivity extends AppCompatActivity implements DetailsAdapter.ListItemClickListener, StepsFragment.ListItemClickListener {

    static String RECIPE_DETAIL = "RECIPE_DETAIL";
    static String RECIPE_STEPS = "RECIPE_STEPS";
    public String recipeName;
    private ArrayList<Recipe> recipe;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        // Check a paused Activity
        if (savedInstanceState == null) {
            Bundle recipeBundle = getIntent().getExtras();
            recipe = new ArrayList<>();
            recipe = recipeBundle.getParcelableArrayList(RECIPE);
            recipeName = recipe.get(0).getName();
            this.getSupportActionBar().setTitle(recipeName);

            //Inflate the Fragments with the JSON data
            final DetailsFragment fragment = new DetailsFragment();
            fragment.setArguments(recipeBundle);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, fragment).addToBackStack(RECIPE_DETAIL)
                    .commit();


            //Check if the Device is a tablet and in case add a second fragment with the steps
           if (findViewById(R.id.recipe_linear_layout).getTag() != null && findViewById(R.id.recipe_linear_layout).getTag().equals("tablet")) {
                final StepsFragment stepsFragment = new StepsFragment();
                stepsFragment.setArguments(recipeBundle);
               fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container2, stepsFragment).addToBackStack(RECIPE_STEPS)
                        .commit();
            }
        } else {
            recipeName = savedInstanceState.getString("Title");
        }
    }


    @Override
    public void onListItemClick(List<Step> stepsOut, int selectedItemIndex, String recipeName) {


        final StepsFragment fragment = new StepsFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();

        getSupportActionBar().setTitle(recipeName);

        Bundle stepBundle = new Bundle();
        stepBundle.putParcelableArrayList(STEP, (ArrayList<Step>) stepsOut);
        stepBundle.putInt(INDEX, selectedItemIndex);
        stepBundle.putString("Title", recipeName);
        fragment.setArguments(stepBundle);

        // in case fo table manages also the steps fragment
        if (findViewById(R.id.recipe_linear_layout).getTag() != null && findViewById(R.id.recipe_linear_layout).getTag().equals("tablet")) {
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container2, fragment).addToBackStack(RECIPE_STEPS)
                    .commit();
        } else {
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment).addToBackStack(RECIPE_STEPS)
                .commit();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("Title", recipeName);
    }

    @Override
    public void onBackPressed() {
        // tell the BackPresse to go to main again after Details Fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 1) {
            //go to Details Screen
            fragmentManager.popBackStack(RECIPE_DETAIL, 0);
        } else {
            //go to Main Activity by finishing the Fragments Management
            finish();
        }
    }
}
