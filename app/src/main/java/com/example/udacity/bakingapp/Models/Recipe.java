package com.example.udacity.bakingapp.Models;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.udacity.bakingapp.Adapters.AllRecipesAdapter;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class Recipe implements Parcelable {
    public  Integer id;
    public  String name;
    public  ArrayList<Ingredient> ingredients;
    public  ArrayList<Step> steps;
    public  Integer servings;
    public  String image;

    public Recipe(Integer id, String name, ArrayList<Ingredient> ingredients, ArrayList<Step> steps, Integer servings, String image) {
        this.id = id;
        this.name = name;
        this.ingredients = ingredients;
        this.steps = steps;
        this.servings = servings;
        this.image = image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeList(this.ingredients);
        dest.writeList(this.steps);
        dest.writeInt(this.servings);
        dest.writeString(this.image);
    }

    protected Recipe(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.ingredients = new ArrayList<>();
        in.readList(this.ingredients, Ingredient.class.getClassLoader());
        this.steps = new ArrayList<>();
        in.readList(this.steps, Step.class.getClassLoader());
        this.servings = in.readInt();
        this.image = in.readString();
    }

    public static final Parcelable.Creator<Recipe> CREATOR = new Parcelable.Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel source) {
            return new Recipe(source);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    // Getters

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    public ArrayList<Step> getSteps() {
        return steps;
    }

    public int getServings() {
        return servings;
    }

    public String getImage() {
        return image;
    }

    //Setters

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIngredients(ArrayList<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public void setSteps(ArrayList<Step> steps) {
        this.steps = steps;
    }

    public void setServings(Integer servings) {
        this.servings = servings;
    }

    public void setImage(String image) {
        this.image = image;
    }


    public static void saveRecipe(Context context, String recipeJson){
        SharedPreferences.Editor editor = context.getSharedPreferences(AllRecipesAdapter.RECIPE_PREF, MODE_PRIVATE).edit();
        editor.putString(AllRecipesAdapter.JSON_RESULT_EXTRA, recipeJson);
        editor.apply();
    }
    public static Recipe loadRecipe(Context context, Recipe recipe) {
        if (recipe ==null) {
            SharedPreferences sharedpreferences = context.getSharedPreferences(AllRecipesAdapter.RECIPE_PREF, MODE_PRIVATE);
            String jsonRecipe = sharedpreferences.getString(AllRecipesAdapter.JSON_RESULT_EXTRA, "");//saved in recipes adapter
            Gson gson = new Gson();
            recipe= gson.fromJson(jsonRecipe, Recipe.class);//recipe
        }
        return recipe;

    }
}
