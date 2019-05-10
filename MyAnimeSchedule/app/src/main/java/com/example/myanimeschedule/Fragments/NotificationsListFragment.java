package com.example.myanimeschedule.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.myanimeschedule.Adapters.NotificationsListAdapter;
import com.example.myanimeschedule.DataStructure.FragmentStack;
import com.example.myanimeschedule.R;

public class NotificationsListFragment extends Fragment {
    RecyclerView recyclerView;
    NotificationsListAdapter adapter;
    FragmentStack fragmentStack;
    BottomNavigationView bottom;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_notifications, container, false);
        return  view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView = getActivity().findViewById(R.id.NrecycleView);
        adapter = new NotificationsListAdapter(getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        fragmentStack = FragmentStack.getInstance();

        bottom = getActivity().findViewById(R.id.SbottomNavigationBar);
        bottom.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            static final String notifications = "Notifications";
            static final String schedule = "Schedule";
            static final String series = "Series";
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if(menuItem.getItemId() == R.id.series){
                    fragmentStack.replaceFragment(FragmentStack.FragmentID.SeriesList, getFragmentManager(), null);
                }else if(menuItem.getItemId() == R.id.schedule){
                    fragmentStack.replaceFragment(FragmentStack.FragmentID.Schedule, getFragmentManager(), null);
                }
                return true;
            }
        });

        bottom.setSelectedItemId(R.id.notifications);
    }
}
