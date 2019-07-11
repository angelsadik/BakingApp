package com.example.udacity.bakingapp.Activities;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Surface;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.udacity.bakingapp.API.RetroInstance;
import com.example.udacity.bakingapp.API.RetroService;
import com.example.udacity.bakingapp.Adapters.AllRecipesAdapter;
import com.example.udacity.bakingapp.Models.Recipe;
import com.example.udacity.bakingapp.R;
import com.google.gson.Gson;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Recipe> recipesArrayList;
    private AllRecipesAdapter recipesAdapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Setting the ArrayList and the RecyclerView
        recipesArrayList = new ArrayList<>();
        recyclerView = findViewById(R.id.recycler_view);

        if(!isLandScape()) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);
        }else{
            int cardWidth = 800;
            GridLayoutManager layoutManager = new GridLayoutManager(this, calculateBestSpanCount(cardWidth));
            recyclerView.setLayoutManager(layoutManager);
        }


        getRecipes();

    }
    private int calculateBestSpanCount(int posterWidth) {
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        float screenWidth = outMetrics.widthPixels;
        return Math.round(screenWidth / posterWidth);
    }

    public void getRecipes() {
        recipesArrayList.clear();

        //As you are using Retrofit, you need not use AsyncTask
        RetroService retroService = RetroInstance.getRetrofitInstance().create(RetroService.class);
        Call<ArrayList<Recipe>> call = retroService.getAllRecipes();
        call.enqueue(new Callback<ArrayList<Recipe>>() {
            @Override
            public void onResponse(Call<ArrayList<Recipe>> call, Response<ArrayList<Recipe>> response) {
                if(response!=null){
                    recipesArrayList=response.body();
//                    String jsonResult = new Gson().toJson(response.body());
                    recipesAdapter = new AllRecipesAdapter(getApplicationContext(),recipesArrayList);//jsonResult
                    recyclerView.setAdapter(recipesAdapter);
                    recipesAdapter.notifyDataSetChanged();
                }
                else {
                    Toast.makeText(getApplication(),"No Response", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<ArrayList<Recipe>> call, Throwable t) {
                Toast.makeText(getApplication(),"Connection Failure", Toast.LENGTH_LONG).show();
            }
        });

    }

        private boolean isLandScape(){
        Display display = ((WindowManager) getSystemService(WINDOW_SERVICE))
                .getDefaultDisplay();
        int rotation = display.getRotation();

        if (rotation == Surface.ROTATION_90
                || rotation == Surface.ROTATION_270) {
            return true;
        }
        return false;
    }

}
