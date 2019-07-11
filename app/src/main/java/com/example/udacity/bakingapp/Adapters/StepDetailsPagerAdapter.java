package com.example.udacity.bakingapp.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.example.udacity.bakingapp.Fragments.StepDetailsFragment;
import com.example.udacity.bakingapp.Models.Step;

import java.util.ArrayList;
import java.util.List;

//tabs adapter for each step to display the step details fragment
public class StepDetailsPagerAdapter extends FragmentPagerAdapter {
    private Context context;
    private ArrayList<Step> stepList;

    public StepDetailsPagerAdapter(Context context, ArrayList<Step> steps, FragmentManager fm) {
        super(fm);
        this.context = context;
        this.stepList = steps;
    }

    @Override
    public Fragment getItem(int position) {
        Bundle arguments = new Bundle();
        arguments.putParcelable(StepDetailsFragment.STEP_KEY, stepList.get(position));
        StepDetailsFragment stepDetailsFragment = new StepDetailsFragment();
        stepDetailsFragment.setArguments(arguments);

        return stepDetailsFragment;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return String.format("Step %d",position);
    }

    @Override
    public int getCount() {
        return stepList.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        super.destroyItem(container, position, object);//removed to prevent destroying items in activity
    }
}
