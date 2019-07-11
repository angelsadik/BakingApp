package com.example.udacity.bakingapp.API;

import com.example.udacity.bakingapp.Models.Recipe;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RetroService {
    @GET("topher/2017/May/59121517_baking/baking.json")
    Call<ArrayList<Recipe>> getAllRecipes();
}
