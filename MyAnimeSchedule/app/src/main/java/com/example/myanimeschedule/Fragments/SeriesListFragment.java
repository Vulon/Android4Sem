package com.example.myanimeschedule.Fragments;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.myanimeschedule.Activity.SearchActivity;
import com.example.myanimeschedule.Adapters.SeriesListAdapter;
import com.example.myanimeschedule.DataStructure.FragmentStack;
import com.example.myanimeschedule.DataStructure.SubscriptionsManager;
import com.example.myanimeschedule.R;
import com.example.myanimeschedule.Utills.HTTPMessenger;
import com.example.myanimeschedule.Utills.NotificationsScheduler;
import com.example.myanimeschedule.Utills.ScheduleReciever;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Observable;
import java.util.Observer;

public class SeriesListFragment extends Fragment {

    SeriesListAdapter adapter;
    SeasonsObserver seasonsObserver;
    EpisodesObserver episodesObserver;
    static boolean loading = false;
    FragmentStack fragmentStack;
    NotificationsScheduler scheduler = NotificationsScheduler.getInstance();

    RecyclerView listView;
    FloatingActionButton searchButton;
    FloatingActionButton updateButton;
    ProgressBar updateProgressBar;
    BottomNavigationView bottom;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.activity_series, container, false);
        listView = view.findViewById(R.id.SLrecycleView);
        listView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new SeriesListAdapter(getContext(), getFragmentManager());
        listView.setAdapter(adapter);
        updateProgressBar = view.findViewById(R.id.updateProgressBar);
        updateProgressBar.setVisibility(View.INVISIBLE);

        bottom = view.findViewById(R.id.SbottomNavigationBar);

        fragmentStack = FragmentStack.getInstance();

        bottom.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            static final String notifications = "Notifications";
            static final String schedule = "Schedule";
            static final String series = "Series";
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if(menuItem.getItemId() == R.id.schedule){
                    fragmentStack.replaceFragment(FragmentStack.FragmentID.Schedule, getFragmentManager(), null);
                }else if(menuItem.getItemId() == R.id.notifications){
                    fragmentStack.replaceFragment(FragmentStack.FragmentID.NotificationsList, getFragmentManager(), null);
                }
                return false;
            }
        });

        searchButton = view.findViewById(R.id.SLsearchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getContext(), SearchActivity.class), 1234);

            }
        });
        updateButton = view.findViewById(R.id.SLupdateButton);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(loading){
                }else{
                    seasonsObserver = new SeasonsObserver();
                    seasonsObserver.loadSeasons(-1);
                }
            }
        });

        return  view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1234){
            SubscriptionsManager smanager = SubscriptionsManager.getInstance(getContext());
            for(int i = 0; i < smanager.id_of_series_to_update.size(); i++){
                SeasonsObserver seasonsObserver = new SeasonsObserver();
                seasonsObserver.loadSeasons(smanager.id_of_series_to_update.get(i));
            }
            smanager.id_of_series_to_update.clear();
            adapter.notifyDataSetChanged();
            scheduler.updateEntryList(getContext());
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public class SeasonsObserver implements Observer{
        SubscriptionsManager smanager;
        HTTPMessenger httpMessenger;
        boolean loading = false;
        int counter = 0;
        int selectedSeriesId = 0;
        public SeasonsObserver() {
            smanager = SubscriptionsManager.getInstance(getContext());
            httpMessenger = HTTPMessenger.getInstance(getContext());
            counter = 0;
        }
        public void loadSeasons(int seriesID){// seriesID of series to update episodes, -1 to update all, 0 to disable episodes update
            if(loading){

            }else{
                updateProgressBar.setVisibility(View.VISIBLE);
                updateButton.setEnabled(false);

                loading = true;
                selectedSeriesId = seriesID;
                SLObservable observable = new SLObservable();
                observable.addObserver(SeasonsObserver.this);
                httpMessenger.getSeasonsInfo(smanager.seriesData.get(0).id, observable, getContext());
            }
        }

        @Override
        public void update(Observable o, Object arg) {
            counter++;
            if(counter < smanager.seriesData.size()){
                SLObservable observable = new SLObservable();
                observable.addObserver(SeasonsObserver.this);
                httpMessenger.getSeasonsInfo(smanager.seriesData.get(counter).id, observable, getContext());
            }else{
                counter = 0;
                adapter.notifyDataSetChanged();
                scheduler.updateEntryList(getContext());
                if(selectedSeriesId == 0){
                    loading = false;
                    updateProgressBar.setVisibility(View.INVISIBLE);
                    updateButton.setEnabled(true);
                }else if(selectedSeriesId == -1){
                    episodesObserver = new EpisodesObserver();
                    episodesObserver.updateAllSeries();
                }else{
                    episodesObserver = new EpisodesObserver();
                    episodesObserver.updateSeries(selectedSeriesId);
                }
            }

        }
    }
    public class SLObservable extends Observable {
        public boolean changeed = false;
        @Override
        public synchronized boolean hasChanged() {
            return changeed;
        }
    }

    public class EpisodesObserver implements Observer {
        int counter;
        SubscriptionsManager smanager;
        HTTPMessenger httpMessenger;

        int seasonNumber;
        boolean updateAllSeries;
        int seriesID;

        public EpisodesObserver() {
            counter = 0;
            seasonNumber = 0;
            smanager = SubscriptionsManager.getInstance(getContext());
            httpMessenger = HTTPMessenger.getInstance(getContext());
            updateAllSeries = false;
        }
        public void updateSeries(int seriesID){
            if(smanager.seriesData.size() < 1){
                return;
            }
            seasonNumber = 0;
            this.seriesID = seriesID;
            smanager.getSeries(seriesID).episodes.clear();
            SLObservable observable = new SLObservable();
            observable.addObserver(EpisodesObserver.this);
            httpMessenger.getEpisodesInfo(seriesID, observable, getContext(),
                    Integer.parseInt(smanager.getSeries(seriesID).seasons.get(seasonNumber)));
        }
        public void updateAllSeries(){
            if(smanager.seriesData.size() < 1){
                return;
            }
            seasonNumber = 0;
            counter = 0;
            seriesID = smanager.seriesData.get(0).id;
            updateAllSeries = true;
            updateSeries(seriesID);
        }
        @Override
        public void update(Observable o, Object arg) {
            seasonNumber++;
            if (seasonNumber < smanager.getSeries(seriesID).seasons.size()){
                SLObservable observable = new SLObservable();
                observable.addObserver(EpisodesObserver.this);
                httpMessenger.getEpisodesInfo(seriesID, observable, getContext(),
                        Integer.parseInt(smanager.getSeries(seriesID).seasons.get(seasonNumber)));
            }else{
                adapter.notifyDataSetChanged();
                scheduler.updateEntryList(getContext());
                if(!updateAllSeries){
                    seasonNumber = 0;
                    //Done updating one series
                    loading = false;
                    updateProgressBar.setVisibility(View.INVISIBLE);
                    updateButton.setEnabled(true);
                }else{//go to next series
                    smanager.sortEpisodes(seriesID);
                    counter++;
                    if(counter < smanager.seriesData.size()){
                        updateSeries(smanager.seriesData.get(counter).id);
                    }else{
                        loading = false;
                        updateAllSeries = false;
                        //Done updating all series
                        updateProgressBar.setVisibility(View.INVISIBLE);
                        updateButton.setEnabled(true);
                    }
                }
            }
        }
    }

}
