package com.example.myanimeschedule.Activity;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.myanimeschedule.Adapters.SeriesListAdapter;
import com.example.myanimeschedule.DataStructure.SubscriptionsManager;
import com.example.myanimeschedule.R;
import com.example.myanimeschedule.Utills.HTTPMessenger;

import java.util.Observable;
import java.util.Observer;

public class SeriesActivity extends AppCompatActivity {
    RecyclerView listView;
    FloatingActionButton searchButton;
    SeriesListAdapter adapter;
    FloatingActionButton updateButton;
    EpisodesObserver episodesObserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_series);
        listView = findViewById(R.id.SLrecycleView);
        listView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapter = new SeriesListAdapter(getApplicationContext());
        listView.setAdapter(adapter);

        searchButton = findViewById(R.id.SLsearchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getApplicationContext(), SearchActivity.class), 1234);
            }
        });
        updateButton = findViewById(R.id.SLupdateButton);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                episodesObserver = new EpisodesObserver();
                episodesObserver.startSeasonLoad();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1234){
            adapter.notifyDataSetChanged();
        }
    }

    public class EpisodesObserver implements Observer{
        int counter;
        int seasonCounter;
        boolean loadingSeasons;
        SubscriptionsManager smanager;
        HTTPMessenger httpMessenger;

        public EpisodesObserver() {
            counter = 0;
            smanager = SubscriptionsManager.getInstance(getApplicationContext());
            httpMessenger = HTTPMessenger.getInstance(getApplicationContext());
        }
        public class SLObservable extends Observable{
            public boolean changeed = false;
            @Override
            public synchronized boolean hasChanged() {
                return changeed;
            }
        }

        public void startSeasonLoad(){
            counter = 0;
            if(smanager.seriesData.size() > 0){
                loadingSeasons = true;
                SLObservable observable = new SLObservable();
                observable.addObserver(EpisodesObserver.this);
                httpMessenger.getSeasonsInfo(smanager.seriesData.get(counter).id, observable, getApplicationContext());
            }
        }

        @Override
        public void update(Observable o, Object arg) {
            if(loadingSeasons){
                counter++;
                if(counter < smanager.seriesData.size()){
                    SLObservable observable = new SLObservable();
                    observable.addObserver(EpisodesObserver.this);
                    httpMessenger.getSeasonsInfo(smanager.seriesData.get(counter).id, observable, getApplicationContext());
                }else{
                    loadingSeasons = false;
                    counter = 0;
                    seasonCounter = 0;
                    smanager.seriesData.get(counter).episodes.clear();
                    SLObservable observable = new SLObservable();
                    observable.addObserver(EpisodesObserver.this);
                    httpMessenger.getEpisodesInfo(smanager.seriesData.get(counter).id, observable, getApplicationContext(),
                            Integer.parseInt(smanager.seriesData.get(counter).seasons.get(seasonCounter)));
                }
            }else{
                seasonCounter++;
                if(seasonCounter < smanager.seriesData.get(counter).seasons.size()){
                    SLObservable observable = new SLObservable();
                    observable.addObserver(EpisodesObserver.this);
                    httpMessenger.getEpisodesInfo(smanager.seriesData.get(counter).id, observable, getApplicationContext(),
                            Integer.parseInt(smanager.seriesData.get(counter).seasons.get(seasonCounter)));
                }else{
                    seasonCounter = 0;
                    counter++;
                    if(counter < smanager.seriesData.size()){
                        smanager.seriesData.get(counter).episodes.clear();
                        SLObservable observable = new SLObservable();
                        observable.addObserver(EpisodesObserver.this);
                        httpMessenger.getEpisodesInfo(smanager.seriesData.get(counter).id, observable, getApplicationContext(),
                                Integer.parseInt(smanager.seriesData.get(counter).seasons.get(seasonCounter)));
                    }else{

                    }
                }

            }
            adapter.notifyDataSetChanged();
        }
    }

}
