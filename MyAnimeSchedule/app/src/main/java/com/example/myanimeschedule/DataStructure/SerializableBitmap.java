package com.example.myanimeschedule.DataStructure;

import android.graphics.Bitmap;

import java.io.Serializable;

public class SerializableBitmap implements Serializable {

    private static final long serialVersionUID = 3735591699658232480L;
    private final int [] pixels;
    private final int width, height;

    public SerializableBitmap(Bitmap bitmap) {
        width = bitmap.getWidth();
        height = bitmap.getHeight();
        pixels = new int[width * height];
        bitmap.getPixels(pixels, 0, width,0,0, width, height);
    }
    public Bitmap getBitmap(){
        return Bitmap.createBitmap(pixels, width, height, Bitmap.Config.RGB_565);
    }
}
