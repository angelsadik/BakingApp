package com.example.udacity.bakingapp;

import android.content.ClipData;
import android.os.Bundle;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.app.FragmentManager;

import com.example.udacity.bakingapp.Activities.RecipeDetailsActivity;
import com.example.udacity.bakingapp.Fragments.RecipeDetailsFragment;
import com.example.udacity.bakingapp.Models.Recipe;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.rule.ActivityTestRule;

import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class RecipeDetailsActivityTest {
    @Rule
    public ActivityTestRule<RecipeDetailsActivity> activityTestRule =
            new ActivityTestRule<>(RecipeDetailsActivity.class);

    @Before
    public void SetUPFragment() {
                /* We defined this method to do anything we want to do before the test, in this example we want to test the views inside Fragment
        /So, we should load that fragment first in the Activity (represented by activityTestRule above)*/
        //TODO Define an object of Fragment
        //TODO  Create a bundle and add the data you want to send to that fragment (in this case the int position of the phrase and the phrase itself), let's use "How are you" phrase for this example
        //Note the keys of the values you'll add to the Bundle should match the keys that PlayerFragment uses to get values from that Bundle
        RecipeDetailsFragment recipeDetailsFragment = RecipeDetailsFragment.newInstance();

        Bundle bundle = new Bundle();
        bundle.putString("name","recipe 1");

        //TODO Send the Bundle you created and filled above to Fragment through setArguments
        recipeDetailsFragment.setArguments(bundle);


        /* Now we simply call getActivity then getSupportFragmentManager to add Fragment as below. Note how we use replace not add,
        because if add will be used Fragment will show above the Fragment that gets loaded by default in the activity (Fragment)*/
        activityTestRule.getActivity()
                .getSupportFragmentManager().beginTransaction().replace(R.id.recipe_details_frame, recipeDetailsFragment).commit();
    }

    // TODO Write a test method using @Test, to test Fragment UI
    @Test
    public void Recycler_scroll_to_position() {

//        try{
//            Thread.sleep(3000);
//        }catch (InterruptedException e){
//            e.printStackTrace();
//        }
        //TODO : Check that the view with the id exists in the hierarchy and is displayed on the screen:
        onView(withId(R.id.title_text)).check(matches(isDisplayed()));//id from fragment.xml
        //TODO 8 (b): Perform a click action on any of the views in PlayerFragment (for example the view with the id eng) and observe the test result
        onView(withId(R.id.steps_recycler)).perform((androidx.test.espresso.ViewAction) actionOnItemAtPosition(0, (ViewAction) click()));

    }

}
