package com.example.myanimeschedule.Fragments;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.example.myanimeschedule.Adapters.EpisodesAdapter;
import com.example.myanimeschedule.DataStructure.SubscriptionsManager;
import com.example.myanimeschedule.R;

public class PageFragment extends Fragment {
    public static int selectedPage = 0;
    public int seriesID;

    private int adapterPosition = 0;
    public void setAdapterPosition(int position, int seriesID){
        this.seriesID = seriesID;
        adapterPosition = position;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        switch (adapterPosition){
            case 1:
                return loadEpisodes(inflater, container, savedInstanceState);
            case 2:
                return loadSettings(inflater, container, savedInstanceState);

                default:
                    return loadDetails(inflater, container, savedInstanceState);

        }
    }
    private View loadDetails(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_series_details, null);
        ImageView banner = view.findViewById(R.id.FDbanner);
        TextView name = view.findViewById(R.id.FDname);
        TextView status = view.findViewById(R.id.FDstatus);
        TextView genre = view.findViewById(R.id.FDgenre);
        TextView network = view.findViewById(R.id.FDnetwork);
        TextView overview = view.findViewById(R.id.FDoverview);
        SubscriptionsManager.SeriesData data = SubscriptionsManager.getInstance(getContext()).getSeries(seriesID);
        banner.setImageBitmap(data.banner.getBitmap());
        name.setText(data.name);
        status.setText("Status: " + data.status);
        String genreLine = "";
        for(String line : data.genre){
            genreLine += ", " + line;
        }
        genre.setText(genreLine);
        network.setText(data.network);
        overview.setText(data.overview);
        return view;
    }
    private View loadEpisodes(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_series_episodes, null);
        RecyclerView listView = view.findViewById(R.id.FDepisodesListView);
        listView.setLayoutManager(new LinearLayoutManager(getContext()));
        SubscriptionsManager.SeriesData  data = SubscriptionsManager.getInstance(getContext()).getSeries(seriesID);
        EpisodesAdapter adapter = new EpisodesAdapter(getContext(), data);
        listView.setAdapter(adapter);
        return  view;
    }
    private View loadSettings(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_series_settings, null);
        TextView realDay = view.findViewById(R.id.FDsettingsRealDayOfWeek);
        TextView realHour = view.findViewById(R.id.FDsettingsRealHour);
        Spinner spinner = view.findViewById(R.id.FDspinner);
        EditText hourPick = view.findViewById(R.id.FDsettingsHourPicker);
        Switch notificationsSwitch = view.findViewById(R.id.FDnotificationsSwitch);
        SubscriptionsManager.SeriesData  data = SubscriptionsManager.getInstance(getContext()).getSeries(seriesID);

        ArrayAdapter<?> arrayAdapter = ArrayAdapter.createFromResource(getContext(), R.array.DaysOfWeek, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setSelection(data.airTime.SelectedDayOfWeek);
        String[] days = getResources().getStringArray(R.array.DaysOfWeek);
        realDay.setText(days[data.airTime.RealDayOfWeek]);
        realHour.setText("Hour: " + Integer.toString(data.airTime.RealHour));
        hourPick.setText(Integer.toString(data.airTime.SelectedHour));
        notificationsSwitch.setChecked(data.airTime.sendNotifications);
        return view;
    }
}
