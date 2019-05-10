package com.example.myanimeschedule.Activity;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.myanimeschedule.Adapters.SeriesListAdapter;
import com.example.myanimeschedule.DataStructure.FragmentStack;
import com.example.myanimeschedule.DataStructure.SubscriptionsManager;
import com.example.myanimeschedule.Fragments.CustomHolderFragment;
import com.example.myanimeschedule.Fragments.SeriesListFragment;
import com.example.myanimeschedule.R;
import com.example.myanimeschedule.Utills.HTTPMessenger;
import com.example.myanimeschedule.Utills.NotificationsScheduler;

import java.util.Observable;
import java.util.Observer;

public class MainActivity extends AppCompatActivity {
    FragmentStack fragmentStack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.empty_layout);
        fragmentStack = FragmentStack.getInstance();

        SubscriptionsManager.getInstance(getApplicationContext()).save(getApplicationContext());
        NotificationsScheduler.getInstance().updateEntryList(getApplicationContext());
        NotificationsScheduler.getInstance().updateNotificationService(getApplicationContext());

        fragmentStack.restartFragment(getSupportFragmentManager());

    }

    @Override
    public void onBackPressed() {
        if(fragmentStack.currentFragmentID == FragmentStack.FragmentID.Details){
            fragmentStack.goBack(getSupportFragmentManager());
            SubscriptionsManager.getInstance(getApplicationContext()).save(getApplicationContext());
            NotificationsScheduler.getInstance().updateEntryList(getApplicationContext());
            NotificationsScheduler.getInstance().updateNotificationService(getApplicationContext());
        }
    }
}
