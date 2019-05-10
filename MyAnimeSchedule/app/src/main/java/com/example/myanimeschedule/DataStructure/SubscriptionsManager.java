package com.example.myanimeschedule.DataStructure;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.example.myanimeschedule.Utills.NotificationsScheduler;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;

import static com.example.myanimeschedule.Utills.NotificationsScheduler.getTimeInMillsFromDate;

public class SubscriptionsManager implements Serializable {

    private static final long serialVersionUID = 8558904023799092980L;
    public ArrayList<SeriesData> seriesData;
    public static final String FILENAME = "seriesData";
    private static SubscriptionsManager link = null;

    public ArrayList<Integer> id_of_series_to_update;


    private SubscriptionsManager(Context context) {
        seriesData = new ArrayList<>();
        id_of_series_to_update = new ArrayList<>();
        load(context);
    }
    public static SubscriptionsManager getInstance(Context context){
        if(link == null){
            link = new SubscriptionsManager(context);
        }
        return link;
    }


    public void addNewSeries(String name, int id, Bitmap banner, String firstAired, String network, int runtime, ArrayList<String>genre, String overview, String status){
        seriesData.add(new SeriesData(name, id, banner, firstAired, network, runtime, genre, overview, status));
        id_of_series_to_update.add(id);

    }
    public void deleteSeries(int seriesID){
        for(int i = 0; i < seriesData.size(); i++){
            if(seriesData.get(i).id == seriesID){
                seriesData.remove(i);
                return;
            }
        }
    }

    public void sortEpisodes(int seriesID){
        SeriesData data = getSeries(seriesID);
        Collections.sort(data.episodes);
    }

    public void save(Context context){
        try{
            FileOutputStream out = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            ObjectOutputStream objStream = new ObjectOutputStream(byteStream);
            objStream.writeObject(seriesData);

            out.write(byteStream.toByteArray());
            out.close();
        }catch (Exception e){
            Log.e("SUBSCRIPTION MANAGER", "SAVE EXC "+e.getMessage());
        }

    }



    public boolean containsID(int id){
        for(SeriesData data : seriesData){
            if(data.id == id)
                return true;
        }
        return false;
    }

    public void removeByID(int id){
        for(SeriesData data : seriesData){
            if(data.id == id){
                seriesData.remove(data);
                return;
            }
        }
    }

    public SeriesData getSeries(int id){
        for(SeriesData data : seriesData){
            if(data.id == id){
                return data;
            }
        }
        return  null;
    }

    public SeriesData.Episode getEpisodeByWeekNumber(int seriesID, int weekNumber){
        SeriesData data = getSeries(seriesID);
        Calendar calendar = GregorianCalendar.getInstance();
        int yearNumber = calendar.get(Calendar.YEAR);
        for(SeriesData.Episode episode : data.episodes){
            calendar.setTimeInMillis(getTimeInMillsFromDate(episode.firstAired));

            if(calendar.get(Calendar.YEAR) != yearNumber){
                continue;
            }

            int episodeWeekNumber = calendar.get(Calendar.WEEK_OF_YEAR);
            if(episodeWeekNumber > weekNumber){
                return  null;
            }
            if (episodeWeekNumber == weekNumber){
                return episode;
            }
        }
        return null;
    }

    public void load(Context context){
        try{
            FileInputStream in = context.openFileInput(FILENAME);
            ObjectInputStream objSream = new ObjectInputStream(in);
            seriesData = (ArrayList<SeriesData>) objSream.readObject();
            in.close();
        }catch (Exception e){
            Log.e("SUBSCRIPTION MANAGER", "LOCAL DATA LOAD FAIL");
        }
    }



    public class SeriesData implements Serializable{

        private static final long serialVersionUID = 2659637657433408317L;
        public String name;
        public int id;
        public SerializableBitmap banner;
        public String firstAired;
        public String network;
        public int runtime;
        public ArrayList<String> genre;
        public String overview;
        public AirTime airTime;
        public String status;


        public ArrayList<String> seasons;
        public ArrayList<Episode> episodes = new ArrayList<>();

        public SeriesData(String name, int id, Bitmap banner, String firstAired, String network, int runtime, ArrayList<String>genre, String overview, String status) {
            this.name = name;
            this.id = id;
            this.banner = new SerializableBitmap(banner);
            this.firstAired = firstAired;
            this.network = network;
            this.runtime = runtime;
            this.genre = genre;
            this.overview = overview;
            this.status = status;
            if(episodes == null){
                episodes = new ArrayList<>();
            }
            if(seasons == null){
                seasons = new ArrayList<>();
            }
        }
        public void setAirTime(int dayOfWeekR, int hourR, int dayOfWeekS, int hourS){
            airTime = new AirTime(dayOfWeekR, hourR, dayOfWeekS, hourS);
        }
        public void setAirTime(int dayOfWeekR, int hourR){
            airTime = new AirTime(dayOfWeekR, hourR);
        }
        public void setSeasons(ArrayList<String> seasons){
            this.seasons = seasons;

        }
        public int getLastSeason(){
            if(seasons == null){
                seasons = new ArrayList<>();
            }
            String result = "0";
            for(String s : seasons){
                if(s.charAt(0) > result.charAt(0)){
                    result = s;
                }
            }
            return Integer.parseInt(result);
        }

        public void addEpisode(int seasonNumber, int episodeNumber, long episodeID, long seasonID, String episodeName, String first_Aired, String episodeOverview){
            episodes.add(new Episode(seasonNumber, episodeNumber, episodeID, seasonID, episodeName, first_Aired, episodeOverview));
        }

        public Episode getLastEpisode(){
            if(episodes == null){
                episodes = new ArrayList<>();
            }
            if(episodes.size() > 0){
                Episode returnEpisode = null;

                GregorianCalendar calendar = (GregorianCalendar) GregorianCalendar.getInstance();
                long lastDate = 0l;
                int  lastseason = getLastSeason();
                for(Episode episode : episodes){

                    if(episode.seasonsNumber == lastseason){
                        if(returnEpisode == null){
                            returnEpisode = episode;
                        }
                        if(episode.firstAired.length() > 0){
                            calendar.set(Calendar.YEAR, Integer.parseInt(episode.firstAired.substring(0,4)));
                            calendar.set(Calendar.MONTH, Integer.parseInt(episode.firstAired.substring(5,7)) - 1);
                            calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(episode.firstAired.substring(8,10)));
                            if(calendar.getTimeInMillis() > lastDate && calendar.getTimeInMillis() <= (new Date()).getTime()){
                                lastDate = calendar.getTimeInMillis();
                                returnEpisode = episode;
                            }
                        }
                    }
                }
                return  returnEpisode;
            }else{
                return null;
            }
        }

        public class AirTime implements Serializable{
            private static final long serialVersionUID = 4351274223016959891L;
            public int RealDayOfWeek;
            public int RealHour;
            public int SelectedDayOfWeek;
            public int SelectedHour;
            public boolean sendNotifications = true;

            public AirTime(int realDayOfWeek, int realHour, int selectedDayOfWeek, int selectedHour) {
                RealDayOfWeek = realDayOfWeek;
                RealHour = realHour;
                SelectedDayOfWeek = selectedDayOfWeek;
                SelectedHour = selectedHour;
                sendNotifications = true;
            }

            public AirTime(int realDayOfWeek, int realHour) {
                RealDayOfWeek = realDayOfWeek;
                RealHour = realHour;
                SelectedDayOfWeek = realDayOfWeek;
                sendNotifications = true;
                SelectedHour = realHour;
            }
        }
        public class Episode implements Serializable, Comparable {
            private static final long serialVersionUID = 4750439792537792170L;
            public int seasonsNumber;
            public int episodeNumber;
            public long episodeID;
            public long seasonID;
            public String episodeName;
            public String firstAired;
            public String episodeOverView;

            @Override
            public int compareTo(Object o) {
                Episode other = (Episode)o;
                if(seasonsNumber < other.seasonsNumber){
                    return -1;
                }else if(seasonsNumber > other.seasonsNumber){
                    return 1;
                }else{
                    if(episodeNumber < other.episodeNumber){
                        return  -1;
                    }else if(episodeNumber > other.episodeNumber){
                        return 1;
                    }else {
                        return 0;
                    }
                }
            }

            public Episode(int seasonsNumber, int episodeNumber, long episodeID, long seasonID, String episodeName, String firstAired, String episodeOverView) {
                this.seasonsNumber = seasonsNumber;
                this.episodeNumber = episodeNumber;
                this.episodeID = episodeID;
                this.seasonID = seasonID;
                this.episodeName = episodeName;
                this.firstAired = firstAired;
                this.episodeOverView = episodeOverView;
            }
        }
    }
}
