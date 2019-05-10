package com.example.myanimeschedule.Adapters;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.example.myanimeschedule.Activity.MainActivity;
import com.example.myanimeschedule.DataStructure.FragmentStack;
import com.example.myanimeschedule.DataStructure.SubscriptionsManager;
import com.example.myanimeschedule.R;
import com.example.myanimeschedule.Utills.NotificationsScheduler;

public class PagerAdapter extends FragmentStatePagerAdapter {
    int seriesID;

    @Override
    public Fragment getItem(int i) {
        Bundle bundle = new Bundle();
        bundle.putInt("seriesID", seriesID);
        switch (i){
            case 0:
                DetailsFragment detailsFragment = new DetailsFragment();
                detailsFragment.setArguments(bundle);
                return detailsFragment;
            case 1:
                EpisodesFragment episodesFragment = new EpisodesFragment();
                episodesFragment.setArguments(bundle);
                return episodesFragment;
            case 2:
                SettingsFragment settingsFragment = new SettingsFragment();
                settingsFragment.setArguments(bundle);
                return settingsFragment;
            default:
                DetailsFragment fragment = new DetailsFragment();
                fragment.setArguments(bundle);
                return fragment;
        }

    }
    public PagerAdapter(FragmentManager fm, int seriesID) {
        super(fm);
        this.seriesID = seriesID;
    }



    @Override
    public int getCount() {
        return 3;
    }

    public static class DetailsFragment extends Fragment{
        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_series_details, null);
            SubscriptionsManager subscriptionsManager = SubscriptionsManager.getInstance(getContext());
            ImageView banner = view.findViewById(R.id.FDbanner);
            TextView name = view.findViewById(R.id.FDname);
            TextView status = view.findViewById(R.id.FDstatus);
            TextView genre = view.findViewById(R.id.FDgenre);
            TextView network = view.findViewById(R.id.FDnetwork);
            TextView overview = view.findViewById(R.id.FDoverview);
            int index = getArguments().getInt("seriesID");
            SubscriptionsManager.SeriesData data = subscriptionsManager.getSeries(index);
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

    }
    public static class EpisodesFragment extends Fragment{
        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            SubscriptionsManager subscriptionsManager = SubscriptionsManager.getInstance(getContext());
            View view = inflater.inflate(R.layout.fragment_series_episodes, null);
            RecyclerView listView = view.findViewById(R.id.FDepisodesListView);
            listView.setLayoutManager(new LinearLayoutManager(getContext()));
            int index = getArguments().getInt("seriesID");
            SubscriptionsManager.SeriesData  data = subscriptionsManager.getSeries(index);
            EpisodesAdapter adapter = new EpisodesAdapter(getContext(), data);
            listView.setAdapter(adapter);
            return  view;
        }
    }
    public static class SettingsFragment extends Fragment{
        SubscriptionsManager subscriptionsManager;
        FragmentStack fragmentStack;
        NotificationsScheduler scheduler;

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_series_settings, null);

            final int index = getArguments().getInt("seriesID");
            subscriptionsManager = SubscriptionsManager.getInstance(getContext());
            scheduler = NotificationsScheduler.getInstance();
            final SubscriptionsManager.SeriesData data = subscriptionsManager.getSeries(index);

            TextView realDay = view.findViewById(R.id.FDsettingsRealDayOfWeek);
            TextView realHour = view.findViewById(R.id.FDsettingsRealHour);
            Button unsibscribeButton = view.findViewById(R.id.FDunsubscribeButton);

            Switch notificationsSwitch = view.findViewById(R.id.FDnotificationsSwitch);
            notificationsSwitch.setChecked(data.airTime.sendNotifications);
            notificationsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    subscriptionsManager.getSeries(data.id).airTime.sendNotifications = isChecked;
                }
            });

            ArrayAdapter<?> arrayAdapter = ArrayAdapter.createFromResource(getContext(), R.array.DaysOfWeek, android.R.layout.simple_spinner_item);
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            Spinner spinner = view.findViewById(R.id.FDspinner);

            spinner.setAdapter(arrayAdapter);
            spinner.setSelection(data.airTime.SelectedDayOfWeek);

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    subscriptionsManager.getSeries(data.id).airTime.SelectedDayOfWeek = position;
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            String[] days = getResources().getStringArray(R.array.DaysOfWeek);
            realDay.setText(days[data.airTime.RealDayOfWeek]);
            realHour.setText("Hour: " + Integer.toString(data.airTime.RealHour));

            final EditText hourPick = view.findViewById(R.id.FDsettingsHourPicker);
            hourPick.setText(Integer.toString(data.airTime.SelectedHour));
            hourPick.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    int hour = -1;
                    try {
                        hour = Integer.parseInt(hourPick.getText().toString());
                        if(hour >= 0 && hour < 24){
                            subscriptionsManager.getSeries(data.id).airTime.SelectedHour = hour;
                            subscriptionsManager.save(getContext());
                            scheduler.updateEntryList(getContext());
                        }
                    }catch (NumberFormatException e){
                        hour = -1;
                    }

                }
            });



            fragmentStack = FragmentStack.getInstance();
            unsibscribeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    subscriptionsManager.removeByID(index);
                    subscriptionsManager.save(getContext());
                    scheduler.updateEntryList(getContext());
                    scheduler.updateNotificationService(getContext());
                    fragmentStack.goBack(getFragmentManager());
                }
            });

            return view;
        }
    }

}
