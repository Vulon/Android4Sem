package com.example.myanimeschedule.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myanimeschedule.DataStructure.FragmentStack;
import com.example.myanimeschedule.DataStructure.SubscriptionsManager;
import com.example.myanimeschedule.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class ScheduleRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    Context context;
    ArrayList<Integer> id_data;
    SubscriptionsManager subscriptionsManager;
    String[] stringArray;

    public ScheduleRecyclerAdapter(Context context, String[] stringArray) {
        long timeSpan = 604800l * 3; // 3 week in seconds - after 3 weeks series considered ended
        this.context = context;
        this.stringArray = stringArray;
        subscriptionsManager = SubscriptionsManager.getInstance(context);
        id_data = new ArrayList<>();
        for(int day = 0; day < 7; day++){
            id_data.add(day);
            for(SubscriptionsManager.SeriesData seriesData : subscriptionsManager.seriesData){
                if(seriesData.airTime.SelectedDayOfWeek == day && seriesData.status.equals("Continuing")){
                    if(seriesData.getLastEpisode() == null){
                        continue;
                    }
                    if(getTimeSpan(seriesData.getLastEpisode().firstAired) <= timeSpan){
                        id_data.add(seriesData.id);

                    }
                }
            }
        }
    }

    long getTimeSpan(String firstAired){
        int year = Integer.parseInt(firstAired.substring(0,4));
        int month = Integer.parseInt(firstAired.substring(5,7));
        int day = Integer.parseInt(firstAired.substring(8, 10));
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.set(year,month,day);
        Date date = new Date();
        long now = date.getTime();
        return  (now - calendar.getTimeInMillis()) / 1000l;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        switch (viewHolder.getItemViewType()){
            case 0: //DAY OF WEEK
                DayOfWeekHolder dayOfWeekHolder = (DayOfWeekHolder) viewHolder;
                dayOfWeekHolder.dayName.setText(stringArray[id_data.get(i)]);
                break;
            case 1:
                SubscriptionsManager.SeriesData seriesData = subscriptionsManager.getSeries(id_data.get(i));
                ScheduleEntryHolder scheduleEntryHolder = (ScheduleEntryHolder) viewHolder;
                scheduleEntryHolder.banner.setImageBitmap(seriesData.banner.getBitmap());
                scheduleEntryHolder.name.setText(seriesData.name);
                if(seriesData.getLastEpisode() != null){
                    scheduleEntryHolder.lastEpisodeNumber.setText("Last episode: " + Integer.toString(seriesData.getLastEpisode().episodeNumber));
                    scheduleEntryHolder.lastAiredDate.setText("Last Aired: " + seriesData.getLastEpisode().firstAired);
                }

                break;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        final Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        if(i == 0){ //DAY OF WEEK
            View itemView = inflater.inflate(R.layout.recycler_series_schedule_dayofweek, viewGroup, false);
            DayOfWeekHolder holder = new DayOfWeekHolder(itemView);
            return holder;
        }else{
            View itemView = inflater.inflate(R.layout.recycler_series_schedule_entry, viewGroup, false);
            ScheduleEntryHolder holder = new ScheduleEntryHolder(itemView);
            return holder;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(id_data.get(position) < 7){
            return  0; //DAY OF WEEK
        }else{
            return  1; //SERIES ENTRY
        }
    }

    @Override
    public int getItemCount() {
        return id_data.size();
    }

    class ScheduleEntryHolder extends RecyclerView.ViewHolder{
        ImageView banner;
        TextView name;
        TextView lastEpisodeNumber;
        TextView lastAiredDate;

        public ScheduleEntryHolder(View viewItem){
            super(viewItem);
            name = itemView.findViewById(R.id.SEname);
            banner = itemView.findViewById(R.id.SEbanner);
            lastEpisodeNumber = itemView.findViewById(R.id.SElastEpisodeNumber);
            lastAiredDate = itemView.findViewById(R.id.SElastUpdated);
        }
    }


    class DayOfWeekHolder extends RecyclerView.ViewHolder{
        TextView dayName;
        public DayOfWeekHolder(View viewItem){
            super(viewItem);
            dayName = viewItem.findViewById(R.id.SEnameOfDay);
        }
    }
}

