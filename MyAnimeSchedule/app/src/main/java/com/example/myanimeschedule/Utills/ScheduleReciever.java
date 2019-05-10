package com.example.myanimeschedule.Utills;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.myanimeschedule.DataStructure.SubscriptionsManager;
import com.example.myanimeschedule.R;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public  class ScheduleReciever extends BroadcastReceiver {
    public static final  String START_ACTION = "com.example.myanimeschedule.START_ACTION";
    NotificationManager mManager;

    public ScheduleReciever() {
    }
    static int num = 0;
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction() == null){
            return;
        }
        if (!intent.getAction().equals(START_ACTION)){
            return;
        }


        NotificationsScheduler scheduler = NotificationsScheduler.getInstance();
        scheduler.loadEntriesData(context);
        scheduler.loadRecordsData(context);

        Calendar calendar = GregorianCalendar.getInstance();
        int weekNumber = calendar.get(Calendar.WEEK_OF_YEAR);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 2;
        String text = "";

        for(int i = 0; i < scheduler.entries.size(); i++){
            NotificationsScheduler.ScheduleEntry entry = scheduler.entries.get(i);
            if(!scheduler.checkRecord(entry.seriesID,weekNumber)){
                if(entry.dayOfWeek < dayOfWeek || (entry.dayOfWeek == dayOfWeek && entry.hour <= hour)){
                    text += entry.name + " episode " + Integer.toString(entry.thisWeekEpisodeNumber) + ";\n";
                    scheduler.addRecord(entry.seriesID, weekNumber);
                }
            }
        }
        scheduler.saveRecordsData(context);

        if(text.length() > 1){
            sendNotification("New episodes", text, context);
        }
        Toast.makeText(context, "Notifications text: " + text, Toast.LENGTH_LONG).show();
        scheduleNextWakeUP(context, scheduler);
    }


    public static void stopService(Context context){
        Intent intent = new Intent(context, ScheduleReciever.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }

    public static void startServiceUP(Context context){
        //stopService(context);
        AlarmManager alarmManager =(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.add(Calendar.SECOND, 5);
        Intent intent = new Intent(context, ScheduleReciever.class);
        intent.setAction(START_ACTION);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmIntent);
    }

    public void scheduleNextWakeUP(Context context, NotificationsScheduler scheduler){
        //stopService(context);
        scheduler.updateMills();
        scheduler.saveEntriesData(context);
        AlarmManager alarmManager =(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, ScheduleReciever.class);
        intent.setAction(START_ACTION);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 5, intent, PendingIntent.FLAG_ONE_SHOT);
        if(scheduler.entries.size() > 0){
            Calendar calendar = GregorianCalendar.getInstance();

            calendar.setTimeInMillis(scheduler.entries.get(0).timeInMills);
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmIntent);
            String line =  "day: " + Integer.toString(calendar.get(Calendar.DATE));
            line += "." + Integer.toString(calendar.get(Calendar.MONTH) + 1) + "\n";
            line += "hour: " + Integer.toString(calendar.get(Calendar.HOUR_OF_DAY));
            line += " for " + scheduler.entries.get(0).name;
            line += " - " + Long.toString(calendar.getTimeInMillis());
            Toast.makeText(context, "WAKEUP AT " + line, Toast.LENGTH_LONG).show();
        }else{
            Calendar calendar = GregorianCalendar.getInstance();
            calendar.add(Calendar.DATE, 1);
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() + 1000 * 60 * 10l, alarmIntent);
        }
    }

    void sendNotification(String title, String text, Context context){
        String CHANNEL_ID = "com.example.myanimeschedule.CHANNEL";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "NOTIFICATIONS CHANNEL";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.notification)
                .setContentTitle(title)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(text))
                .setAutoCancel(true);
        getManager(context).notify(12, builder.build());
    }

    NotificationManager getManager(Context context) {
        if (mManager == null) {
            mManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return mManager;
    }



}
