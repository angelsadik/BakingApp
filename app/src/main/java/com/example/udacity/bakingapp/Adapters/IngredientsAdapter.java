package com.example.udacity.bakingapp.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.udacity.bakingapp.Models.Ingredient;
import com.example.udacity.bakingapp.R;

import java.util.ArrayList;
import java.util.List;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.IngredientViewHolder> {
    private Context context;
    private ArrayList<Ingredient> ingredientsList;

    public IngredientsAdapter(Context context, ArrayList<Ingredient> ingredientsList) {
        this.context = context;
        this.ingredientsList = ingredientsList;
    }

    @Override
    public IngredientsAdapter.IngredientViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_ingredients_listitem, parent, false);

        return new IngredientsAdapter.IngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(IngredientsAdapter.IngredientViewHolder holder, int position) {
        holder.ingredientQuantity.setText("\u2022 "+ingredientsList.get(position).getQuantity());
        holder.ingredientMeasure.setText(""+ingredientsList.get(position).getMeasure());
        holder.ingredientName.setText(""+ingredientsList.get(position).getIngredient());

    }

    @Override
    public int getItemCount() {
        return ingredientsList.size();
    }

    public class IngredientViewHolder extends RecyclerView.ViewHolder {
        public TextView ingredientName;
        public TextView ingredientQuantity;
        public TextView ingredientMeasure;

        public IngredientViewHolder(View itemView) {
            super(itemView);
            ingredientName=itemView.findViewById(R.id.ingredient);
            ingredientQuantity=itemView.findViewById(R.id.quantity);
            ingredientMeasure=itemView.findViewById(R.id.measure);
        }
    }
}
