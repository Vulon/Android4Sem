package com.example.lab2;

import android.graphics.Bitmap;

public class TechData {
    public String name;
    public String iconUrl;
    public String description;
    public Bitmap smallImage;
    public Bitmap bigImage;
    public boolean isSmallImageLoaded = false;
    public boolean isBigImageLoaded = false;


    public TechData(String name, String iconUrl, String description) {
        this.name = name;
        this.iconUrl = iconUrl;
        this.description = description;
    }

    public void loadSmallImage(Bitmap image){
        smallImage = image;
        isSmallImageLoaded = true;
    }
    public void loadBigImage(Bitmap image){
        bigImage = image;
        isBigImageLoaded = true;
    }

}
