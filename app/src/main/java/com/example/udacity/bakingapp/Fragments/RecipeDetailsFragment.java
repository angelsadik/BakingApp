package com.example.udacity.bakingapp.Fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.udacity.bakingapp.Activities.RecipeDetailsActivity;
import com.example.udacity.bakingapp.Adapters.IngredientsAdapter;
import com.example.udacity.bakingapp.Adapters.StepsAdapter;
import com.example.udacity.bakingapp.Models.Ingredient;
import com.example.udacity.bakingapp.Models.Recipe;
import com.example.udacity.bakingapp.Models.Step;
import com.example.udacity.bakingapp.R;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static com.example.udacity.bakingapp.Activities.RecipeDetailsActivity.RECIPES_EXTRA_KEY;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link //RecipeDetailsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RecipeDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecipeDetailsFragment extends Fragment {

    private OnStepClickListener listener;

    public static final String RECIPE_KEY = "recipe";
    int recipePosition;

    private Recipe recipe;

    TextView title;
    LinearLayoutManager linearLayoutManager;
    IngredientsAdapter ingredientsAdapter;
    RecyclerView ingredientsRecyclerView;
    StepsAdapter stepsAdapter;
    RecyclerView stepsRecyclerView;


//    private final BroadcastReceiver networkChangeReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            if (recipe == null) {
//                loadRecipes();//using retrofit
//            }
//        }
//    };

    public RecipeDetailsFragment() {
        // Required empty public constructor
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param //recipePosition Parameter 1.
     * @return A new instance of fragment RecipeDetailsFragment.
     */
    // Rename and change types and number of parameters
    public static RecipeDetailsFragment newInstance() {
        RecipeDetailsFragment fragment = new RecipeDetailsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null && getArguments().containsKey(RECIPE_KEY)) {
            recipe = getArguments().getParcelable(RECIPE_KEY);
        }
        // Retain the fragment on rotation
        setRetainInstance(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recipe_details, container, false);
        title =view.findViewById(R.id.title_text);
        ingredientsRecyclerView = view.findViewById(R.id.ingredient_recycler);
        stepsRecyclerView = view.findViewById(R.id.steps_recycler);

        if (savedInstanceState != null && savedInstanceState.containsKey(RECIPE_KEY)) {
            recipe = savedInstanceState.getParcelable(RECIPE_KEY);
        }
        title.setText(recipe.getName());

        linearLayoutManager = new LinearLayoutManager(getContext());
        ingredientsRecyclerView.setLayoutManager(linearLayoutManager);
        ingredientsAdapter = new IngredientsAdapter(getContext(), recipe.ingredients);
        ingredientsRecyclerView.setAdapter(ingredientsAdapter);

        linearLayoutManager = new LinearLayoutManager(getContext());
        stepsRecyclerView.setLayoutManager(linearLayoutManager);
//        stepsAdapter = new StepsAdapter(getContext(), recipe.steps);
//        stepsRecyclerView.setAdapter(stepsAdapter);
        stepsRecyclerView.setAdapter(new StepsAdapter(getActivity().getApplicationContext(), recipe.steps, new Listeners.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                listener.onStepSelected(recipe.steps.get(position));
            }
        }));

        return view;
    }

    public interface OnStepClickListener {
        void onStepSelected(Step step);//implemented in the RecipeDetailsActivity
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnStepClickListener) {
            listener = (OnStepClickListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnStepClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        Logger.d("onDestroyView");
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        if (recipe != null ) {//&& !recipe.isEmpty())
            outState.putParcelable(RECIPE_KEY, recipe);
        }
    }

}

