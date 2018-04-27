package com.example.android.bakingapp.data;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by casab on 15/04/2018.
 */

public interface RecipesInterface {
    @GET("baking.json")
    public Call<ArrayList<Recipe>> getRecipies();
}
