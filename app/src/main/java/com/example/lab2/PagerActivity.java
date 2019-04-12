package com.example.lab2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

public class PagerActivity extends AppCompatActivity {

    ViewPager pager = null;
    RequestQueue queue;
    static PagerAdapter adapter = null;
    boolean isLoading = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pager);
        pager = (ViewPager)findViewById(R.id.pager);
        if(adapter == null){
            adapter = new PagerAdapter(getSupportFragmentManager());
        }
        pager.setAdapter(adapter);
        queue = Volley.newRequestQueue(getApplicationContext());
        adapter.setPageID(DataManager.pageSelected);
        pager.setCurrentItem(DataManager.pageSelected);

        Log.i("ON CREATE PAGER", Integer.toString(DataManager.pageSelected));
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
           @Override
           public void onPageScrolled(int i, float v, int i1) {
           }

           @Override
           public void onPageSelected(int i) {
               if(!isLoading){
                   Log.i("New selected page", Integer.toString(i));
                   Log.i("Tech selected", DataManager.list.get(i).name);
                   if(DataManager.list.get(i).isBigImageLoaded){
                       DataManager.pageSelected = i;
                       adapter.setPageID(i);
                   }else{
                       loadBigImage(i);
                       isLoading = true;
                   }
               }
           }

           @Override
           public void onPageScrollStateChanged(int i) {

           }
        });
    }

    void loadBigImage(final int pageID){
        String url = "https://raw.githubusercontent.com/wesleywerner/ancient-tech/02decf875616dd9692b31658d92e64a20d99f816/src/images/tech/";
        url += DataManager.list.get(pageID).iconUrl;
        ImageRequest request = new ImageRequest(url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                DataManager.loadBigImage(response, pageID);
                DataManager.pageSelected = pageID;
                adapter.setPageID(pageID);
                Log.i("Loaded", Integer.toString(pageID));
                isLoading = false;
            }
        }, 100, 100, ImageView.ScaleType.CENTER_CROP, Bitmap.Config.RGB_565, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("LOAD SMALL EXC", Integer.toString(pageID));
                Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.tech);
                DataManager.loadBigImage(image, pageID);
                Log.i("IMG LOADED", "Image Loaded");
                isLoading = false;

            }
        });
        queue.add(request);
    }
}
