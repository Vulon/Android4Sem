package com.example.lab2;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class DataManager {
    public static ArrayList<TechData> list = new ArrayList<>();

    public static void addNew(String name, String iconUrl, String description){
        list.add(new TechData(name, iconUrl, description));
    }
    public static int size(){
        return list.size();
    }
    public static TechData getItem(int i){
        if(i < list.size() && i > 0){
            return list.get(i);
        }
        else return null;
    }
    public static int pageSelected;
    public static void loadSmallImage(Bitmap image, int index){
        list.get(index).loadSmallImage(image);
    }
    public static void loadBigImage(Bitmap image, int index){
        list.get(index).loadBigImage(image);
    }

}
