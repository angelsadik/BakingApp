package com.example.udacity.bakingapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.example.udacity.bakingapp.Models.Recipe;
import com.example.udacity.bakingapp.R;

import java.util.ArrayList;

import com.example.udacity.bakingapp.Activities.RecipeDetailsActivity;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.squareup.picasso.Picasso;


public class AllRecipesAdapter extends RecyclerView.Adapter<AllRecipesAdapter.RecipeViewHolder> {
    public static final String RECIPE_PREF="Recipe_Pref";
    public static final String JSON_RESULT_EXTRA = "JSON_RESULT_EXTRA";

    private Context context;
    private ArrayList<Recipe> recipesArrayList;
    String recipeJson;

    public AllRecipesAdapter(Context context, ArrayList<Recipe> recipesArrayList) {
        this.context = context;
        this.recipesArrayList = recipesArrayList;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recipe_list_item, viewGroup, false);

        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder recipeViewHolder, int i) {
        recipeViewHolder.recipeName.setText(recipesArrayList.get(i).getName());
        recipeViewHolder.servings.setText("Servings: "+ recipesArrayList.get(i).getServings());

        String recipeImage = recipesArrayList.get(i).getImage();
        if (!recipeImage.isEmpty()) {
            Picasso.get()
                    .load(recipeImage)
                    .placeholder(R.drawable.cooking)
                    .into(recipeViewHolder.recipeImage);
        }

        String jsonResult = new Gson().toJson(recipesArrayList);//=in Main:toJson(response.body());
        recipeJson = jsonToString(jsonResult, recipeViewHolder.getAdapterPosition());
        Recipe.saveRecipe( context, recipeJson);

    }

    @Override
    public int getItemCount() {
        return recipesArrayList.size();
    }

    class RecipeViewHolder extends RecyclerView.ViewHolder {

        public TextView recipeName;
        public TextView servings;
        public AppCompatImageView recipeImage;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);

            recipeName = itemView.findViewById(R.id.recipe_name_text);
            servings = itemView.findViewById(R.id.servings_text);
            recipeImage = itemView.findViewById(R.id.recipe_image);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, RecipeDetailsActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra(RecipeDetailsActivity.RECIPES_EXTRA_KEY, recipesArrayList.get(getAdapterPosition()));//bundle
                    intent.putExtra(JSON_RESULT_EXTRA,recipeJson);
                    context.startActivity(intent);
                }
            });

        }
    }

    // Get selected Recipe as Json String
    private String jsonToString(String jsonResult, int position){
        JsonElement jsonElement = new JsonParser().parse(jsonResult);
        JsonArray jsonArray = jsonElement.getAsJsonArray();
        JsonElement recipeElement = jsonArray.get(position);
        return recipeElement.toString();
    }
}
