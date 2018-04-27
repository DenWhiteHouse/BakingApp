package com.example.android.bakingapp.data;

/**
 * Created by casab on 23/04/2018.
 */

//Using Retrofit to take the JSON

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public final class RetroClient {
    private static final String ROOT_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/";
    static RecipesInterface RecipInt;

    public static RecipesInterface getRetrofitInstance() {
        Gson gson = new GsonBuilder().create();
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        RecipInt = new Retrofit.Builder()
                .baseUrl(ROOT_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .callFactory(httpClientBuilder.build())
                .build().create(RecipesInterface.class);
        return RecipInt;
    }
}
