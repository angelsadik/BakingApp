package com.example.udacity.bakingapp.Activities;

import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.udacity.bakingapp.Adapters.StepDetailsPagerAdapter;
import com.example.udacity.bakingapp.Fragments.StepDetailsFragment;
import com.example.udacity.bakingapp.Models.Step;
import com.example.udacity.bakingapp.R;

import java.util.ArrayList;
import java.util.List;

public class StepDetailsActivity extends AppCompatActivity {

    public static final String STEPS_EXTRA_KEY = "step_extra_key";
    public static final String STEPS_POSITION_EXTRA_KEY = "step_position_extra_key";

    private ArrayList<Step> stepList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_details);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        stepList = getIntent().getParcelableArrayListExtra(STEPS_EXTRA_KEY);
        int position = getIntent().getIntExtra(STEPS_POSITION_EXTRA_KEY,0);

        StepDetailsPagerAdapter adapter = new StepDetailsPagerAdapter(getApplicationContext(), stepList, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.step_viewpager);
//        viewPager.setOffscreenPageLimit(2);////////***************?????????//
        viewPager.setAdapter(adapter);
        TabLayout tabLayout = findViewById(R.id.step_tabs);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setCurrentItem(position);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
