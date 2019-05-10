package com.example.myanimeschedule.Utills;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.myanimeschedule.DataStructure.SubscriptionsManager;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;

public class NotificationsScheduler implements Serializable{
    private static final long serialVersionUID = -8882765702441747412L;

    public ArrayList<ScheduleEntry> entries;
    public ArrayList<NotificationRecord> records;

    private static NotificationsScheduler link;
    private static final String ENTRIES_FILENAME = "schedule";
    private static final String RECORDS_FILENAME = "records";
    private NotificationsScheduler(){
        if(entries == null){
            entries = new ArrayList<>();
            records = new ArrayList<>();
        }
    }
    public static NotificationsScheduler getInstance(){
        if(link == null){
            link = new NotificationsScheduler();
        }
        return link;
    }

    public void updateMills(){
        Calendar calendar = GregorianCalendar.getInstance();
        long now = (new Date()).getTime();
        for(ScheduleEntry entry : entries){
            while (entry.timeInMills < now){
                calendar.setTimeInMillis(entry.timeInMills);
                calendar.add(Calendar.DAY_OF_YEAR, 7);
                entry.timeInMills = calendar.getTimeInMillis();
            }

        }
        Collections.sort(entries);
    }

    public void updateNotificationService(Context context){
        ScheduleReciever.startServiceUP(context);
    }

    public void updateEntryList(Context context){
        entries.clear();
        long timeSpan = 604800l * 3;
        SubscriptionsManager manager = SubscriptionsManager.getInstance(context);
        manager.load(context);

        Calendar calendar = GregorianCalendar.getInstance();

        int weekNumber = calendar.get(Calendar.WEEK_OF_YEAR);
        int yearNumber = calendar.get(Calendar.YEAR);
        for(SubscriptionsManager.SeriesData data : manager.seriesData){
            if(data.status.equals("Continuing") && data.getLastEpisode() != null && getTimeSpan(data.getLastEpisode().firstAired) <= timeSpan
            && data.airTime.sendNotifications){
                manager.sortEpisodes(data.id);
                int season = data.getLastSeason();
                SubscriptionsManager.SeriesData.Episode thisWeekEpisode = null;

                for(SubscriptionsManager.SeriesData.Episode episode : data.episodes){
                    thisWeekEpisode = episode;
                    calendar.setTimeInMillis(getTimeInMillsFromDate(episode.firstAired));

                    if(episode.seasonsNumber != season || calendar.get(Calendar.YEAR) != yearNumber){
                        continue;
                    }

                    int episodeWeekNumber = calendar.get(Calendar.WEEK_OF_YEAR);
                    if(episodeWeekNumber > weekNumber){
                        break;
                    }
                    if (episodeWeekNumber == weekNumber){
                        thisWeekEpisode = episode;
                        break;
                    }
                }
                calendar.set(Calendar.HOUR_OF_DAY, data.airTime.SelectedHour);
                calendar.set(Calendar.DAY_OF_WEEK, data.airTime.SelectedDayOfWeek + 2);
                calendar.set(Calendar.MINUTE, 0);
                entries.add(new ScheduleEntry(data.id, data.name, data.airTime.SelectedDayOfWeek, data.airTime.SelectedHour
                , thisWeekEpisode.episodeNumber, calendar.getTimeInMillis()));
                String line = "Added: " + data.name +" " + Integer.toString(calendar.get(Calendar.DATE)) +".";
                line += Integer.toString(calendar.get(Calendar.MONTH)) + " - h: " + Integer.toString(calendar.get(Calendar.HOUR_OF_DAY));
                Log.i("SCHEDULER UPDATE", line);
            }
        }
        updateMills();
        Collections.sort(entries);
        saveEntriesData(context);
    }

    public void saveEntriesData(Context context){
        try{
            FileOutputStream out = context.openFileOutput(ENTRIES_FILENAME, Context.MODE_PRIVATE);
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            ObjectOutputStream objStream = new ObjectOutputStream(byteStream);
            objStream.writeObject(entries);
            out.write(byteStream.toByteArray());
            out.close();
            Log.i("SCHEDULER", "Saved entries: " + Integer.toString(entries.size()));
        }catch (Exception e){
            Log.i("DATA SAVE FAIL", e.getMessage());
        }
    }
    public void loadEntriesData(Context context){
        try{
            FileInputStream in = context.openFileInput(ENTRIES_FILENAME);
            ObjectInputStream objSream = new ObjectInputStream(in);
            entries = (ArrayList<ScheduleEntry>) objSream.readObject();
            in.close();
            Log.i("SCHEDULER", "Loaded entries: " + Integer.toString(entries.size()));
        }catch (Exception e){
            Toast.makeText(context, "FILED TO LOAD RECORDS", Toast.LENGTH_LONG).show();
            Log.e("DATA LOAD FAIL", "DATA LOAD FAIL");
        }
        Collections.sort(entries);
    }

    public void saveRecordsData(Context context){
        try{
            FileOutputStream out = context.openFileOutput(RECORDS_FILENAME, Context.MODE_PRIVATE);
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            ObjectOutputStream objStream = new ObjectOutputStream(byteStream);
            objStream.writeObject(records);
            out.write(byteStream.toByteArray());
            out.close();
            Log.i("SCHEDULER", "Saved records " + Integer.toString(records.size()));
            Toast.makeText(context, "Records save error", Toast.LENGTH_LONG);

        }catch (Exception e){
            Log.i("DATA SAVE FAIL", e.getMessage());
            Toast.makeText(context, "Records save error", Toast.LENGTH_LONG);
        }
    }

    public void loadRecordsData(Context context){
        try{
            FileInputStream in = context.openFileInput(RECORDS_FILENAME);
            ObjectInputStream objSream = new ObjectInputStream(in);
            records = (ArrayList<NotificationRecord>) objSream.readObject();
            in.close();
            Log.i("SCHEDULER", "Loaded records " + Integer.toString(records.size()));
        }catch (Exception e){
            Toast.makeText(context, "FILED TO LOAD RECORDS", Toast.LENGTH_LONG).show();
            records.clear();
            Log.e("DATA LOAD FAIL", "DATA LOAD FAIL");
        }
    }


    public class ScheduleEntry implements Serializable, Comparable{
        private static final long serialVersionUID = 2597696897315671631L;
        public int seriesID;
        public String name;
        public int dayOfWeek;
        public int hour;
        public int thisWeekEpisodeNumber;
        public long timeInMills;

        public ScheduleEntry(int seriesID, String name, int dayOfWeek, int hour, int thisWeekEpisodeNumber, long timeInMills) {
            this.seriesID = seriesID;
            this.name = name;
            this.dayOfWeek = dayOfWeek;
            this.hour = hour;
            this.thisWeekEpisodeNumber = thisWeekEpisodeNumber;
            this.timeInMills = timeInMills;
        }

        @Override
        public int compareTo(Object o) {
            ScheduleEntry other = (ScheduleEntry)o;
            if(timeInMills < other.timeInMills){
                return -1;
            }else if(timeInMills > other.timeInMills){
                return 1;
            }else{
                return 0;
            }
        }
    }

    public boolean checkRecord(int seriesID, int thisWeekNumber){
        for(NotificationRecord notificationRecord : records){
            if(notificationRecord.seriesID == seriesID && notificationRecord.thisWeekNumber == thisWeekNumber){
                return  true;
            }
        }
        return false;
    }
    public void addRecord(int seriesID, int thisWeekNumber){
        records.add(new NotificationRecord(seriesID, thisWeekNumber));
    }
    public void deleteOldRecords(int thisWeekNumber){
        for(NotificationRecord notificationRecord : records){
            if(notificationRecord.thisWeekNumber < thisWeekNumber){
                records.remove(notificationRecord);
            }
        }
    }

    public class NotificationRecord implements Serializable{
        private static final long serialVersionUID = -8278697658641802540L;
        public int seriesID;
        public int thisWeekNumber;
        public boolean seen;
        public NotificationRecord(int seriesID, int thisWeekNumber) {
            this.seriesID = seriesID;
            this.thisWeekNumber = thisWeekNumber;
            seen = false;
        }
    }

    public static long getTimeSpan(String firstAired){
        Date date = new Date();
        long now = date.getTime();
        return  (now - getTimeInMillsFromDate(firstAired)) / 1000l;
    }
    public static long getTimeInMillsFromDate(String date){
        if (date.length() <9){
            return (new Date().getTime());
        }
        int year = Integer.parseInt(date.substring(0,4));
        int month = Integer.parseInt(date.substring(5,7)) - 1;
        int day = Integer.parseInt(date.substring(8, 10));
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.set(year,month,day);
        return calendar.getTimeInMillis();
    }

}
