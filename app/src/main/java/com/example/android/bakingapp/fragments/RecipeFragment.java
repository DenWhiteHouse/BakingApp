package com.example.android.bakingapp.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.bakingapp.MainActivity;
import com.example.android.bakingapp.R;
import com.example.android.bakingapp.RecipeAdapter;
import com.example.android.bakingapp.data.Recipe;
import com.example.android.bakingapp.data.RecipesInterface;
import com.example.android.bakingapp.data.RetroClient;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.android.bakingapp.MainActivity.FETCHEDRECIPES;

/**
 * Created by casab on 27/04/2018.
 */

public class RecipeFragment extends Fragment {
    //Constructor Empty
    public RecipeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final RecyclerView recyclerView;

        View mainView = inflater.inflate(R.layout.recipe_fragment_body, container, false);
        recyclerView = (RecyclerView) mainView.findViewById(R.id.recipeRecycler);
        final RecipeAdapter recipesAdapter = new RecipeAdapter((MainActivity) getActivity());

        //Check if LandView
        if (mainView.getTag() != null && mainView.getTag().equals("phone-land")) {
            GridLayoutManager mLayoutManager = new GridLayoutManager(getContext(), 4);
            recyclerView.setLayoutManager(mLayoutManager);
        } else {
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(mLayoutManager);
        }

        //Getting the JSON Object with Retrofit
        RecipesInterface RecipeInt = RetroClient.getRetrofitInstance();
        Call<ArrayList<Recipe>> recipe = RecipeInt.getRecipies();

        recipe.enqueue(new Callback<ArrayList<Recipe>>() {
            @Override
            public void onResponse(Call<ArrayList<Recipe>> call, Response<ArrayList<Recipe>> response) {
                ArrayList<Recipe> recipes = response.body();
                Bundle recipesBundle = new Bundle();
                recipesBundle.putParcelableArrayList(FETCHEDRECIPES, recipes);
                recipesAdapter.setRecipe(recipes, getContext());
                //Setting the Adapter inside onResponde to avoid misAdapting due enqueu's delay
                recyclerView.setAdapter(recipesAdapter);
            }

            @Override
            public void onFailure(Call<ArrayList<Recipe>> call, Throwable t) {
                Log.v("Retrofit has failed ", t.getMessage());
            }
        });
        //return the adapted view
        return mainView;
    }
}
