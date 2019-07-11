package com.example.udacity.bakingapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.udacity.bakingapp.Activities.RecipeDetailsActivity;
import com.example.udacity.bakingapp.Activities.StepDetailsActivity;
import com.example.udacity.bakingapp.Fragments.Listeners;
import com.example.udacity.bakingapp.Fragments.RecipeDetailsFragment;
import com.example.udacity.bakingapp.Fragments.StepDetailsFragment;
import com.example.udacity.bakingapp.Models.Recipe;
import com.example.udacity.bakingapp.Models.Step;
import com.example.udacity.bakingapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.example.udacity.bakingapp.Activities.StepDetailsActivity.*;


//Steps list adapter for the Main Activity recycler View
public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.StepViewHolder> {

    private Context context;
    private ArrayList<Step> stepsList;
    private Listeners.OnItemClickListener onItemClickListener;

    public StepsAdapter(Context context, ArrayList<Step> stepsList, Listeners.OnItemClickListener onItemClickListener) {
        this.context = context;
        this.stepsList = stepsList;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public StepViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_steps_listitem, parent, false);

        return new StepsAdapter.StepViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StepViewHolder holder, final int position) {
        holder.stepOrder.setText(""+position+".");
        holder.stepName.setText(stepsList.get(position).getShortDescription());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null)
                    onItemClickListener.onItemClick(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return stepsList.size();
    }

    public class StepViewHolder extends RecyclerView.ViewHolder {
        public TextView stepOrder;
        public TextView stepName;

        public StepViewHolder(final View itemView) {
            super(itemView);
            stepOrder=itemView.findViewById(R.id.step_order);
            stepName=itemView.findViewById(R.id.step_name);
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (getAdapterPosition() == RecyclerView.NO_POSITION) return;
//
//                    notifyItemChanged(selectedPos);
//                    selectedPos = getLayoutPosition();
//                    notifyItemChanged(selectedPos);

//                    if(!isLandScape())
//                    {
//                        Intent intent = new Intent(context, StepDetailsActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        intent.putExtra(StepDetailsActivity.STEPS_EXTRA_KEY, stepsList);//.get(getAdapterPosition()));
//                        int i = getAdapterPosition();
//                        intent.putExtra(StepDetailsActivity.STEPS_POSITION_EXTRA_KEY, i);
//                        context.startActivity(intent);
//                    } else {
//                        //if landscape with two pane
//                        Bundle arguments = new Bundle();
//                        arguments.putParcelable(StepDetailsFragment.STEP_KEY, stepsList.get(getAdapterPosition()));
//
//                        FragmentManager fragmentManager = ((RecipeDetailsActivity) context).getSupportFragmentManager();
//                        StepDetailsFragment stepDetailsFragment = (StepDetailsFragment) fragmentManager.findFragmentByTag(STEP_DETAILS_FRAGMENT_TAG);
////                            stepDetailsFragment = StepDetailsFragment.newInstance();
//                            stepDetailsFragment.setStep(stepsList.get(getAdapterPosition()));//selected step
//                        stepDetailsFragment.setArguments(arguments);
//
//                        fragmentManager.beginTransaction()
//                                    .replace(R.id.step_details_frame, stepDetailsFragment,STEP_DETAILS_FRAGMENT_TAG)
//                                    .commit();
//
//                    }
//
//
//                }
//            });
        }
    }
//    private boolean isLandScape(){
//        Display display = ((WindowManager) context.getSystemService(WINDOW_SERVICE))
//                .getDefaultDisplay();
//        int rotation = display.getRotation();
//
//        if (rotation == Surface.ROTATION_90
//                || rotation == Surface.ROTATION_270) {
//            return true;
//        }
//        return false;
//    }
}
