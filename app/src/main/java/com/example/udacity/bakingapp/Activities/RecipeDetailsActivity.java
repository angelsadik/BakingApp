package com.example.udacity.bakingapp.Activities;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.udacity.bakingapp.Adapters.AllRecipesAdapter;
import com.example.udacity.bakingapp.Fragments.StepDetailsFragment;
import com.example.udacity.bakingapp.Models.Recipe;
import com.example.udacity.bakingapp.Models.Step;
import com.example.udacity.bakingapp.R;
import com.example.udacity.bakingapp.Fragments.RecipeDetailsFragment;
import com.example.udacity.bakingapp.Widget.AppWidgetProvider;

public class RecipeDetailsActivity extends AppCompatActivity implements RecipeDetailsFragment.OnStepClickListener{

    public static final String RECIPES_EXTRA_KEY = "recipe_extra_key";
    private static final String RECIPE_DETAILS_FRAGMENT_TAG = "RecipeDetailsFragment";
    private static final String STEP_DETAILS_FRAGMENT_TAG = "StepDetailsFragment";

    RecipeDetailsFragment recipeDetailsFragment;

    public static boolean landscapeMode;
    View divider;

    Recipe recipe;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        recipe = getIntent().getParcelableExtra(RECIPES_EXTRA_KEY);
//        bundle = getIntent().getParcelableExtra(RECIPES_EXTRA_KEY);//=recipe
////        if (bundle != null && bundle.containsKey(RECIPES_EXTRA_KEY)) {
//            recipe = bundle.getParcelable(RECIPES_EXTRA_KEY);
////        }
        //Activity launched from Widget
        boolean isWidget = getIntent().getBooleanExtra(AppWidgetProvider.WIDGET_EXTRA,false);
        if (recipe ==null && isWidget) {
            recipe= getIntent().getParcelableExtra(RECIPES_EXTRA_KEY);
        }
        if (savedInstanceState != null && savedInstanceState.containsKey(RecipeDetailsFragment.RECIPE_KEY)) {
            recipe = savedInstanceState.getParcelable(RecipeDetailsFragment.RECIPE_KEY);
        }
        bundle= new Bundle();
        bundle.putParcelable(RECIPES_EXTRA_KEY,recipe);

        //if phone, just add fragment, otherwise, add both recipe and step fragment
        // Set up the correct layout
        setupFragmentForPortrait();
        if(isLandscapeLayout()){
            setupFragmentsForLandscape();
        }

    }

    private void setupFragmentForPortrait() {
        FragmentManager fragmentManager = getSupportFragmentManager();
//      retrieve the instance of a previously added fragment with the given tag regardless of the container it was added to.
        recipeDetailsFragment = (RecipeDetailsFragment)fragmentManager.findFragmentByTag(RECIPE_DETAILS_FRAGMENT_TAG);
//        doesn't mean the fragment is visible or added to the container, which means you should make an extra check
        if (recipeDetailsFragment == null ) {//&& recipeDetailsFragment.isAdded()){
            recipeDetailsFragment = RecipeDetailsFragment.newInstance();
            recipeDetailsFragment.setRecipe(recipe);
            fragmentManager.beginTransaction()
                    .add(R.id.recipe_details_frame, recipeDetailsFragment, RECIPE_DETAILS_FRAGMENT_TAG)
                    .commit();
        }else{
            fragmentManager.beginTransaction()
                    .replace(R.id.recipe_details_frame, recipeDetailsFragment, RECIPE_DETAILS_FRAGMENT_TAG)
                    .commit();
        }

        recipeDetailsFragment.setArguments(bundle);

    }

    private void setupFragmentsForLandscape() {
        FragmentManager fragmentManager = getSupportFragmentManager();

//        StepDetailsFragment stepDetailsFragment = (StepDetailsFragment)fragmentManager.findFragmentByTag(STEP_DETAILS_FRAGMENT_TAG);
        //if null the create new:
        StepDetailsFragment stepDetailsFragment = StepDetailsFragment.newInstance();
        stepDetailsFragment.setStep(recipe.getSteps().get(0));//first step
        bundle.putParcelable(StepDetailsFragment.STEP_KEY, recipe.getSteps().get(0));
        stepDetailsFragment.setArguments(bundle);

            fragmentManager.beginTransaction()
                    .add(R.id.step_details_frame, stepDetailsFragment,STEP_DETAILS_FRAGMENT_TAG)
                    .commit();

    }

    @Override
    public void onStepSelected(Step step) {
        if(!isLandscapeLayout())
        {
            Intent intent = new Intent(this, StepDetailsActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(StepDetailsActivity.STEPS_EXTRA_KEY, recipe.getSteps());//.get(getAdapterPosition()));
            int i = recipe.getSteps().indexOf(step);
            intent.putExtra(StepDetailsActivity.STEPS_POSITION_EXTRA_KEY, i);
            startActivity(intent);
        }
        else {
            //if landscape with two pane
            FragmentManager fragmentManager = getSupportFragmentManager();
            StepDetailsFragment stepDetailsFragment = (StepDetailsFragment) fragmentManager.findFragmentByTag(STEP_DETAILS_FRAGMENT_TAG);
            stepDetailsFragment = StepDetailsFragment.newInstance();
            stepDetailsFragment.setStep(step);//selected step
            bundle.putParcelable(StepDetailsFragment.STEP_KEY, step);
            stepDetailsFragment.setArguments(bundle);
            fragmentManager.beginTransaction()
                    .replace(R.id.step_details_frame, stepDetailsFragment,STEP_DETAILS_FRAGMENT_TAG)
                    .commit();

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.recipe_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.add_to_widget) {
//
            String recipeJson = getIntent().getStringExtra(AllRecipesAdapter.JSON_RESULT_EXTRA);
            Recipe.saveRecipe(getApplicationContext(),recipeJson);

            // Get widget manager
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, AppWidgetProvider.class));
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.recipe_name);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.recipe_image);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds,R.id.recipe_ingredients);

            AppWidgetProvider.updateAppWidget(getApplicationContext(), appWidgetManager, recipe, appWidgetIds);

            Toast.makeText(getApplication(),"Recipe added to Widget", Toast.LENGTH_LONG).show();
            return true;
        } else
            return super.onOptionsItemSelected(item);
    }

    /**
     * Determines if the layout landscape with two panes and divider
     */
    private boolean isLandscapeLayout() {
        divider = findViewById(R.id.divider);//or steps framelayout
        if (divider != null) {
            landscapeMode = true;
            return true;
        } else {
            landscapeMode = false;
            return false;
        }

    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("landscape", landscapeMode);

        if (recipe != null) {
            outState.putParcelable(RECIPES_EXTRA_KEY, recipe);
        }

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        landscapeMode=savedInstanceState.getBoolean("landscape");

        recipe = (Recipe) savedInstanceState.getParcelable(RECIPES_EXTRA_KEY);
    }
}
