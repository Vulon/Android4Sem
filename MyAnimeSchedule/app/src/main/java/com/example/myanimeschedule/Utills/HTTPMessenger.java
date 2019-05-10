package com.example.myanimeschedule.Utills;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myanimeschedule.DataStructure.ObservableData;
import com.example.myanimeschedule.DataStructure.ObservableTitleData;
import com.example.myanimeschedule.Adapters.SearchListAdapter;
import com.example.myanimeschedule.DataStructure.SubscriptionsManager;
import com.example.myanimeschedule.Fragments.SeriesListFragment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class HTTPMessenger implements Serializable {

    private static final String APIKEY = "ZT6C3S8W5A1Y9U9L";
    //private static final String SEARCHKEY = "3b6959f1";
    private static final String BASEURL = "https://api.thetvdb.com";
    public static final String LANGUAGE = "en";
    private static final long serialVersionUID = -4159529515369945655L;
    //https://www.thetvdb.com/banners/
    //private static final String SERACHURL = "http://www.omdbapi.com/";
    private static String token;
    private static Date tokenExpDate;
    private static String username;
    private static String userkey;
    public static RequestQueue queue = null;
    private static ObservableData observableData;
    public static ObservableTitleData observableTitleData;
    private static void initialize(Context context) {
        queue = Volley.newRequestQueue(context);
    }


    private static HTTPMessenger link = null;
    private HTTPMessenger(Context context){
        initialize(context);
    }
    public static HTTPMessenger getInstance(Context context){
        if(link == null){
            link = new HTTPMessenger(context);
        }
        return link;
    }


    public void loadData(String userName, String userKey, String toKen, long expDate){
        username = userName;
        userkey = userKey;
        token = toKen;
        tokenExpDate = new Date(expDate);
    }

    public void login(String userName, String userKey, final ObservableData observable){
        username = userName;
        userkey = userKey;
        tokenExpDate = new Date();
        tokenExpDate.setTime(tokenExpDate.getTime() + 86400000l);
        if(queue != null){
            JSONObject parameter = new JSONObject();
            try{
                parameter.put("apikey", APIKEY);
                parameter.put("userkey", userkey);
                parameter.put("username", username);
            }catch (org.json.JSONException e){
                Log.e("JSON ERROR !!!!!" , "Some paramtere error");
            }
            String url = BASEURL + "/login";
            observableData = observable;
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, parameter,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try{
                                if(response.has("Error")){
                                    observableData.setError(response.getString("Error"));
                                    observableData.notifyObservers();
                                }else{
                                    token = response.getString("token");
                                    observableData.setResponse(token);
                                    observableData.notifyObservers();
                                }
                            }catch (org.json.JSONException e){
                                observableData.setError("Error in token");
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            observableData.setError("Wrong login / password");
                            observableData.notifyObservers();
                        }
                    }
            );
            queue.add(request);
        }else{
            observableData = observable;
            observableData.setError("Not initialized");
            observableData.notifyObservers();
        }
    }

   public void searchSeries(String name, final SearchListAdapter adapter){

        String url = BASEURL + "/search/series?name=" + name;
        JSONObject parameters = new JSONObject();
        try{
            parameters.put("name", name);
        }catch (Exception e){
            Log.i("PARAMETERS EXCEPTION", e.toString());
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if(response.has("Error")){
                    Log.i("REQUEST ERROR", response.toString());
                }else{
                    try{
                        JSONArray jsonArray = response.getJSONArray("data");
                        ArrayList<JSONObject> arrayList = new ArrayList<>();
                        for(int i = 0; i < jsonArray.length(); i++){
                            arrayList.add(jsonArray.getJSONObject(i));
                        }
                        adapter.loadData(arrayList);
                        adapter.notifyDataSetChanged();
                    }catch (Exception e){
                        Log.i("REQUEST PARSE EXC", e.toString());
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                adapter.seriesNotFound();
                adapter.notifyDataSetChanged();
            }
        }){
            @Override
            public Map<String, String> getHeaders(){
                Map<String, String> params = new HashMap<String, String>();
                params.put("Accept", "application/json");
                params.put("Accept-Language", LANGUAGE);
                params.put("Authorization", "Bearer " + token);
                return params;
            }
        };
        queue.add(request);
   }

   public void getSeriesInfo(String id, final ObservableTitleData observable){
        String url = BASEURL + "/series/" + id;
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    if(response.has("Error")){
                        observable.setError(response.getString("Error"));
                    }else{
                        JSONObject d = response.getJSONObject("data");
                        String banner = d.getString("banner");
                        String name = d.getString("seriesName");
                        String firstAired = d.getString("firstAired");
                        String genre = d.getString("genre");
                        String overView = d.getString("overview");
                        String network = d.getString("network");
                        String status = d.getString("status");
                        long lastUpdate = d.getLong("lastUpdated");
                        String airDay = d.getString("airsDayOfWeek");

                        observable.setResponse(banner, name, firstAired, genre, overView, network, status, lastUpdate, airDay, response.toString());
                        observable.notifyObservers();
                    }
                }catch (Exception e){
                    Log.i("SERIES INFO EXC", e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                observable.setError(error.getMessage());
                observable.notifyObservers();
            }
        }){
            @Override
            public Map<String, String> getHeaders(){
                Map<String, String> params = new HashMap<String, String>();
                params.put("Accept", "application/json");
                params.put("Accept-Language", LANGUAGE);
                params.put("Authorization", "Bearer " + token);
                return params;
            }
        };
        queue.add(request);

   }

   public void getEpisodesInfo(final int seriesId, final SeriesListFragment.SLObservable observable, final Context context, final int seasonNumber){
       String url = BASEURL + "/series/" + Integer.toString(seriesId) + "/episodes/query?airedSeason=" + Integer.toString(seasonNumber);
       JsonObjectRequest request = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
           @Override
           public void onResponse(JSONObject response) {
               try {
                   JSONArray jsonArray = response.getJSONArray("data");
                   SubscriptionsManager smanager = SubscriptionsManager.getInstance(context);
                   for(int i = 0; i < jsonArray.length(); i++){
                       JSONObject object = jsonArray.getJSONObject(i);
                       int episodeNumber = Integer.parseInt(object.getString("airedEpisodeNumber"));
                       long episodeID = Long.parseLong(object.getString("id"));
                       long seasonsID = Long.parseLong(object.getString("airedSeasonID"));
                       String episodeName = object.getString("episodeName");
                       String firstAired = object.getString("firstAired");
                       String overView = object.getString("overview");
                       smanager.getSeries(seriesId).addEpisode(seasonNumber, episodeNumber,episodeID, seasonsID, episodeName, firstAired, overView);

                   }
                   smanager.save(context);
                   observable.changeed = true;
                   observable.notifyObservers();
               }catch (Exception e){
                   Log.e("EPISODES JSON EXC", e.toString());
               }

           }
       }, new Response.ErrorListener() {
           @Override
           public void onErrorResponse(VolleyError error) {
                Log.e("EPISODE REQ EXC", error.toString());
           }
       }){
           @Override
           public Map<String, String> getHeaders() throws AuthFailureError {
               Map<String, String> params = new HashMap<String, String>();
               params.put("Accept", "application/json");
               params.put("Accept-Language", LANGUAGE);
               params.put("Authorization", "Bearer " + token);
               return params;
           }
       };
       queue.add(request);
   }
   public void getSeasonsInfo(final int id, final SeriesListFragment.SLObservable observable, final Context context){
        String url = BASEURL + "/series/" + Integer.toString(id) + "/episodes/summary";
        JsonObjectRequest request = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                    try{
                        JSONObject temp = response.getJSONObject("data");
                        JSONArray array = temp.getJSONArray("airedSeasons");
                        ArrayList<String> seasons = new ArrayList<>();
                        for(int i = 0; i < array.length(); i++){
                            seasons.add(array.getString(i));
                        }
                        SubscriptionsManager.getInstance(context).getSeries(id).setSeasons(seasons);
                        SubscriptionsManager.getInstance(context).save(context);
                        observable.changeed = true;
                        observable.notifyObservers();
                    }catch (Exception e){
                        Log.e("JSON EPISODE EXC", e.toString());
                    }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("EPISODE REQUEST ERR", error.toString());
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Accept", "application/json");
                params.put("Accept-Language", LANGUAGE);
                params.put("Authorization", "Bearer " + token);
                return params;
            }
        };
        queue.add(request);
   }

    public String getToken() {
        return token;
    }

    public Date getTokenExpDate() {
        return tokenExpDate;
    }

    public String getUsername() {
        return username;
    }

    public String getUserkey() {
        return userkey;
    }
}
