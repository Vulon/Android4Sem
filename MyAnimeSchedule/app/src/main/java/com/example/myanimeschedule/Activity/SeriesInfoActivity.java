package com.example.myanimeschedule.Activity;

import android.graphics.Bitmap;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.example.myanimeschedule.DataStructure.ObservableTitleData;
import com.example.myanimeschedule.DataStructure.SubscriptionsManager;
import com.example.myanimeschedule.R;
import com.example.myanimeschedule.Utills.HTTPMessenger;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;


public class SeriesInfoActivity extends AppCompatActivity {
    private boolean seriesAlreadySubscribed = false;

    private ImageView banner;
    private TextView name;
    private TextView firstAired;
    private TextView genre;
    private TextView overView;
    private TextView network;
    private TextView status;
    private TextView airDay;
    private FloatingActionButton backButton;
    private FloatingActionButton subcribeButton;

    private InfoObserver observer;
    private HTTPMessenger messenger;

    ObservableTitleData data = null;
    private String jsonString = null;
    Bitmap bannerBitmap;
    private int seriesID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_series_info);
        {
            banner = (ImageView)findViewById(R.id.ItitleBanner);
            name = (TextView)findViewById(R.id.ItitleName);
            firstAired = (TextView)findViewById(R.id.ItitleFirstAired);
            genre = (TextView)findViewById(R.id.ItitleGenre);
            overView = (TextView)findViewById(R.id.ItitleOverView);
            network = (TextView)findViewById(R.id.ItitleNetwork);
            status = (TextView)findViewById(R.id.ItitleStatus);
            airDay = (TextView)findViewById(R.id.ItitleAirDay);
        }

        SubscriptionsManager subscriptionsManager = SubscriptionsManager.getInstance(getApplicationContext());


            observer = new InfoObserver();
            data = new ObservableTitleData();
            data.addObserver(observer);
            messenger = HTTPMessenger.getInstance(getApplicationContext());
            messenger.getSeriesInfo(getIntent().getStringExtra("ID"), data);
            seriesID = Integer.parseInt(getIntent().getStringExtra("ID"));

        subcribeButton = (FloatingActionButton)findViewById(R.id.IsubscribeButton);

        if(subscriptionsManager.containsID(seriesID)){
                seriesAlreadySubscribed = true;
                subcribeButton.setImageDrawable(getResources().getDrawable(R.drawable.unsubscribe));
            }else {
                seriesAlreadySubscribed = false;
                subcribeButton.setImageDrawable(getResources().getDrawable(R.drawable.subscribe));
            }





        backButton = (FloatingActionButton)findViewById(R.id.IbackButton);
        backButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        subcribeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (data != null && data.isResponse){
                    if(seriesAlreadySubscribed){
                        removeSeries(seriesID);
                    }else{
                        addSeries();
                    }
                    finish();
                }
            }
        });

    }

    void addSeries(){
        SubscriptionsManager series = SubscriptionsManager.getInstance(getApplicationContext());
        if(data != null){
            if(data.isResponse){
                try{
                    JSONObject temp = new JSONObject(jsonString);
                    JSONObject object = temp.getJSONObject("data");
                    ArrayList<String> genrelist = new ArrayList<>();
                    try{
                        JSONArray array = new JSONArray(data.genre);
                        for(int i = 0; i < array.length(); i++){
                            genrelist.add(array.getString(i));
                        }
                    }catch (Exception e){
                        Log.e("SERIES INFO ACT", "ADD SERIES JSON ERROR" +e.getMessage());
                    }
                    String timeLine = object.getString("airsTime");
                    int hour;
                    if(timeLine.contains("PM")){
                        int index = timeLine.indexOf(":");
                        hour = Integer.parseInt(timeLine.substring(0, index));
                        hour += 12;
                    }else{
                        int index = timeLine.indexOf(":");
                        hour = Integer.parseInt(timeLine.substring(0, index));
                    }
                    String dayLine = object.getString("airsDayOfWeek");
                    String[] stringArray = getResources().getStringArray(R.array.DaysOfWeek);
                    int dayOfWeekIndex = 0;
                    for(int i = 0; i < stringArray.length; i++){
                        if (stringArray[i].equals(dayLine)){
                            dayOfWeekIndex = i;
                            break;
                        }
                    }
                    series.addNewSeries(
                            data.name, object.getInt("id"), bannerBitmap, data.firstAired, data.network, object.getInt("runtime"),
                            genrelist, data.overView, data.status);
                    series.getSeries(object.getInt("id")).setAirTime(dayOfWeekIndex, hour);


                    series.save(getApplicationContext());
                }catch (Exception e){
                    Log.e("SERIES INFO ACT", "ADD SERIES SOME EXC "+e.getMessage());
                }
            }
        }
    }
    void removeSeries(int id){
        SubscriptionsManager series = SubscriptionsManager.getInstance(getApplicationContext());
        series.removeByID(id);
        series.save(getApplicationContext());
    }

    private class InfoObserver implements Observer{
        @Override
        public void update(Observable o, Object arg) {
            ObservableTitleData data = (ObservableTitleData)o;
            if(data.isResponse){
                try{
                    String bannerURL = "https://www.thetvdb.com/banners/" + data.bannerURL;
                    loadBanner(bannerURL);
                    name.setText(data.name);
                    firstAired.setText("First Aired: "+ data.firstAired);
                    String genreString = data.genre;
                    genreString = genreString.replace("[", "");
                    genreString = genreString.replace("]", "");
                    genre.setText(genreString);
                    overView.setText(data.overView);
                    network.setText("Network: "+data.network);
                    status.setText("Status: " + data.status);
                    airDay.setText("Air day: " + data.airDay);
                    jsonString = data.jsonString;



                }catch (Exception e){
                    Log.e("SERIES INFO ACT", "INFO OBSERVER ON UPDATE "+ e.toString());
                }
            }else{
                Log.e("SERIES INFO ACT", "INFO OBSERVER ON UPDATE "+data.errorMessage);
            }

        }
    }

    void loadBanner(String url){
        ImageRequest request = new ImageRequest(url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                banner.setImageBitmap(response);
                bannerBitmap = response;
            }
        }, 0, 0, ImageView.ScaleType.CENTER_CROP, Bitmap.Config.RGB_565, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        messenger.queue.add(request);
    }
}
