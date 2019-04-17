package com.example.myanimeschedule.Activity;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.myanimeschedule.Adapters.PagerAdapter;
import com.example.myanimeschedule.DataStructure.SubscriptionsManager;
import com.example.myanimeschedule.R;

public class DetailsActivity extends AppCompatActivity {
    ViewPager pager;
    PagerAdapter adapter;
    SubscriptionsManager manager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        manager = SubscriptionsManager.getInstance(getApplicationContext());
        pager = findViewById(R.id.DviewPager);
        adapter = new PagerAdapter(getSupportFragmentManager(), getIntent().getIntExtra("SelectedSeriesID", manager.seriesData.get(0).id));

        pager.setAdapter(adapter);

    }
}
