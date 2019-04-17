package com.example.myanimeschedule.DataStructure;

import java.util.Observable;

public class ObservableData extends Observable {
    public String errorMsg = null;
    public boolean isError = false;
    public boolean isResponse = false;
    public String response = null;
    public void setError(String error){
        errorMsg = error;
        isError = true;
    }
    public void setResponse(String response){
        this.response = response;
        isResponse = true;
    }

    @Override
    public synchronized boolean hasChanged() {
        return isError || isResponse;
    }
    public void clear(){
        isResponse = false;
        isError = false;
        errorMsg = "";
        response = "";
        deleteObservers();
    }
}
