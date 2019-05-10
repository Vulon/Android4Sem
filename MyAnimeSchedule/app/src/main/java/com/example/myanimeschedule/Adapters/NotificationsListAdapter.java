package com.example.myanimeschedule.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myanimeschedule.DataStructure.SubscriptionsManager;
import com.example.myanimeschedule.R;
import com.example.myanimeschedule.Utills.NotificationsScheduler;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class NotificationsListAdapter extends RecyclerView.Adapter<NotificationsListAdapter.NotificationViewHolder>{
    NotificationsScheduler scheduler;
    SubscriptionsManager subscriptionsManager;
    ArrayList<NotificationsScheduler.NotificationRecord> records;

    public NotificationsListAdapter(Context context) {
        scheduler = NotificationsScheduler.getInstance();
        subscriptionsManager = SubscriptionsManager.getInstance(context);
        scheduler.loadRecordsData(context);
        records = scheduler.records;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.recycler_notifications_item, viewGroup, false);

        NotificationViewHolder holder = new NotificationViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder notificationViewHolder, int i) {
        int seriesID = records.get(i).seriesID;
        SubscriptionsManager.SeriesData seriesData = subscriptionsManager.getSeries(seriesID);
        notificationViewHolder.banner.setImageBitmap(seriesData.banner.getBitmap());
        notificationViewHolder.name.setText(seriesData.name);
        SubscriptionsManager.SeriesData.Episode episode = subscriptionsManager.getEpisodeByWeekNumber(seriesID, records.get(i).thisWeekNumber);
        if(episode != null){
            notificationViewHolder.lastEpisode.setText("Episode number: " + Integer.toString(episode.episodeNumber));
            notificationViewHolder.episodeName.setText(episode.episodeName);
            Calendar calendar = GregorianCalendar.getInstance();
            calendar.setTimeInMillis(NotificationsScheduler.getTimeInMillsFromDate(episode.firstAired));
            calendar.set(Calendar.HOUR_OF_DAY, seriesData.airTime.SelectedHour);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.DAY_OF_WEEK, seriesData.airTime.SelectedDayOfWeek + 2);
            calendar.set(Calendar.WEEK_OF_YEAR, records.get(i).thisWeekNumber);
            String line = "Date: " + Integer.toString(calendar.get(Calendar.DATE));
            line += "." + Integer.toString(calendar.get(Calendar.MONTH));
            line += "." + Integer.toString(calendar.get(Calendar.YEAR));
            line += " " + Integer.toString(calendar.get(Calendar.HOUR_OF_DAY)) + ":00";
            notificationViewHolder.notifDate.setText(line);
        }
    }

    @Override
    public int getItemCount() {
        return records.size();
    }

    class NotificationViewHolder extends RecyclerView.ViewHolder{
        ImageView banner;
        TextView name;
        TextView lastEpisode;
        TextView notifDate;
        TextView episodeName;
        public NotificationViewHolder(View view){
            super(view);
            banner = view.findViewById(R.id.NIbanner);
            name = view.findViewById(R.id.NIname);
            lastEpisode = view.findViewById(R.id.NIepisodeNumber);
            notifDate = view.findViewById(R.id.NInotificationDate);
            episodeName = view.findViewById(R.id.NIepisodeName);
        }
    }
}
