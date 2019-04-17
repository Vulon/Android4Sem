package com.example.myanimeschedule.DataStructure;

import java.util.Observable;

public class ObservableTitleData extends Observable {
    public String jsonString;
    public String bannerURL;
    public String name;
    public String firstAired;
    public String genre;
    public String overView;
    public String network;
    public String status;
    public long lastUpdate;
    public String airDay;
    public boolean isError = false;
    public boolean isResponse = false;
    public String errorMessage;
    public synchronized void setResponse(String bannerURL, String name, String firstAired, String genre, String overView, String network, String status, long lastUpdate, String airDay, String jsonString) {
        this.bannerURL = bannerURL;
        this.name = name;
        this.firstAired = firstAired;
        this.genre = genre;
        this.overView = overView;
        this.network = network;
        this.status = status;
        this.lastUpdate = lastUpdate;
        this.airDay = airDay;
        isResponse = true;
        this.jsonString = jsonString;

    }

    public synchronized  void setError(String errorMessage){
        isError = true;
        this.errorMessage = errorMessage;
    }

    @Override
    public synchronized boolean hasChanged() {
        return isError || isResponse;
    }
}
