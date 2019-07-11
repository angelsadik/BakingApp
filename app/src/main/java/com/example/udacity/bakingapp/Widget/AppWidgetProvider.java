package com.example.udacity.bakingapp.Widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.RemoteViews;

import com.example.udacity.bakingapp.Activities.MainActivity;
import com.example.udacity.bakingapp.Activities.RecipeDetailsActivity;
import com.example.udacity.bakingapp.Models.Ingredient;
import com.example.udacity.bakingapp.Models.Recipe;
import com.example.udacity.bakingapp.R;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Implementation of App Widget functionality.
 */
public class AppWidgetProvider extends android.appwidget.AppWidgetProvider {

    public static final String WIDGET_EXTRA ="WidgetExtra";

    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                Recipe recipe, int [] appWidgetIds) {

        Intent intent;
        String name="";
        String ingredients="";
        if(recipe==null) {
            recipe = Recipe.loadRecipe(context, recipe);
        }
        //still null
        if(recipe==null) {
//            Create an Intent to launch MainActivity then add it to a PendingIntent
                intent = new Intent(context, MainActivity.class);

        }
        else {

            intent = new Intent(context, RecipeDetailsActivity.class);
            intent.putExtra(WIDGET_EXTRA,true);
            intent.putExtra(RecipeDetailsActivity.RECIPES_EXTRA_KEY,recipe);

            name=recipe.getName();
            StringBuilder stringBuilder = new StringBuilder();
            ArrayList<Ingredient> ingredientList = recipe.getIngredients();
            for(Ingredient ingredient : ingredientList){
                String quantity = String.valueOf(ingredient.getQuantity());
                String measure = ingredient.getMeasure();
                String ingredientName = ingredient.getIngredient();
                String line = quantity + " " + measure + " " + ingredientName;
                stringBuilder.append( line + "\n");
            }
            ingredients = stringBuilder.toString();
        }//end else

        //Get the layout of the App Widget and inflate it into a RemoteViews object
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.app_widget);

        //TODO 1: widget ingredients
        views.setTextViewText(R.id.recipe_name, name);
        views.setImageViewResource(R.id.recipe_image, R.drawable.cooking);
        views.setTextViewText(R.id.recipe_ingredients,ingredients);
//
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        //Call setOnClickPendingIntent on the RemoteViews object and provide the view that will be clicked and the pending intent to be launched
        views.setOnClickPendingIntent(R.id.appwidget_main_layout, pendingIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetIds, views);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.recipe_name);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.recipe_image);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.recipe_ingredients);

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
            updateAppWidget(context, appWidgetManager,null, appWidgetIds);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

