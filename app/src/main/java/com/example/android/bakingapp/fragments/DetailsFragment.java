package com.example.android.bakingapp.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingapp.DetailsAdapter;
import com.example.android.bakingapp.R;
import com.example.android.bakingapp.RecipeDetailsActivity;
import com.example.android.bakingapp.data.Ingredient;
import com.example.android.bakingapp.data.Recipe;
import com.example.android.bakingapp.widget.WidgetIntent;

import java.util.ArrayList;
import java.util.List;

import static com.example.android.bakingapp.MainActivity.SELECTED_RECIPES;

/**
 * Created by casab on 27/04/2018.
 */

public class DetailsFragment extends Fragment {
    ArrayList<Recipe> recipe;
    String recipeName;

    public DetailsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RecyclerView recyclerView;
        TextView textView;

        recipe = new ArrayList<>();

        //CHECK IF THERE IS A SAVED INSTANCE
        if (savedInstanceState != null) {
            recipe = savedInstanceState.getParcelableArrayList(SELECTED_RECIPES);

        } else {
            recipe = getArguments().getParcelableArrayList(SELECTED_RECIPES);
        }

        //Get the ingridients of the Recipe
        List<Ingredient> ingredients = recipe.get(0).getIngredients();
        recipeName = recipe.get(0).getName();

        //Binding the view and the Text to fill with the ingredients
        View rootView = inflater.inflate(R.layout.recipe_details_fragment_body, container, false);
        textView = (TextView) rootView.findViewById(R.id.recipeDetailsText);

        ArrayList<String> recipeIngredientsForWidgets = new ArrayList<>();

        // Making the text for the scrollView of the Ingridients
        for(int i=0;i<ingredients.size();i++){
            textView.append(ingredients.get(i).getIngredient());
            textView.append(" "+ingredients.get(i).getQuantity().toString() );
            textView.append(" "+ingredients.get(i).getMeasure()+"\n");

            //Making Ingridients for the Widget
            recipeIngredientsForWidgets.add(ingredients.get(i).getIngredient() +"\n"
                    + ingredients.get(i).getQuantity().toString()
                    + ingredients.get(i).getMeasure()+"\n");
        }

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recipeDetailRecycler);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);

        DetailsAdapter mRecipeDetailAdapter = new DetailsAdapter((RecipeDetailsActivity) getActivity());
        recyclerView.setAdapter(mRecipeDetailAdapter);
        mRecipeDetailAdapter.setRecipe(recipe, getContext());

        //Intent into to the widget
        WidgetIntent.startBakingService(getContext(),recipeIngredientsForWidgets);
        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle currentState) {
        super.onSaveInstanceState(currentState);
        currentState.putParcelableArrayList(SELECTED_RECIPES, recipe);
        currentState.putString("Title", recipeName);
    }
}
