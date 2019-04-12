package com.example.lab2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private RequestQueue queue;
    boolean secondActivityStarted = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        queue = Volley.newRequestQueue(getApplicationContext());
        loadTextData();
    }

    private void loadTextData(){

        String url = "https://raw.githubusercontent.com/wesleywerner/ancient-tech/02decf875616dd9692b31658d92e64a20d99f816/src/data/techs.ruleset.json";
        JsonArrayRequest request = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.i("RESPONSE", "Got some response");
                Log.i("Response length", Integer.toString(response.length()));
                DataManager.list.clear();
                for(int i = 1; i < response.length(); i++){
                    try{
                        JSONObject object = response.getJSONObject(i);
                        String name = object.getString("name");
                        String iconUrl = object.getString("graphic");
                        String description = "";
                        if(object.has("helptext")){
                            description = object.getString("helptext");
                        }
                        DataManager.addNew(name, iconUrl, description);

                    }catch (Exception e){
                        Log.i("Text LOADER ERR", e.getMessage());
                    }
                }
                Log.i("Data manager size", Integer.toString(DataManager.size()));
                loadSmallImage(0);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("SOME RESP ERROR", "I do not know what happened");
            }
        });
        queue.add(request);
    }

    private void loadSmallImage(final int index){
        if(DataManager.list.get(index).isSmallImageLoaded){
            if(index < DataManager.size() - 1){
                loadSmallImage(index + 1);
            }else{
                if(!secondActivityStarted){
                    Intent intent = new Intent(getApplicationContext(), ListActivity.class);
                    startActivity(intent);
                    secondActivityStarted = true;
                }
            }
        }else{
            String url = "https://raw.githubusercontent.com/wesleywerner/ancient-tech/02decf875616dd9692b31658d92e64a20d99f816/src/images/tech/";
            url += DataManager.list.get(index).iconUrl;
            ImageRequest request = new ImageRequest(url, new Response.Listener<Bitmap>() {
                @Override
                public void onResponse(Bitmap response) {
                    DataManager.loadSmallImage(response, index);
                    Log.i("Loaded", Integer.toString(index));
                    if(index < DataManager.size() - 1){
                        loadSmallImage(index + 1);
                    }else{
                        if(!secondActivityStarted){
                            Intent intent = new Intent(getApplicationContext(), ListActivity.class);
                            startActivity(intent);
                            secondActivityStarted = true;
                        }
                    }
                }
            }, 64, 64, ImageView.ScaleType.CENTER_CROP, Bitmap.Config.RGB_565, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.i("LOAD SMALL EXC", Integer.toString(index));
                    Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.tech);
                    //Bitmap resized = Bitmap.createBitmap(image, 0, 0, 64, 64);
                    DataManager.loadSmallImage(image, index);
                    Log.i("IMG LOADED", "Image Loaded");
                    if(index < DataManager.size() - 1){
                        loadSmallImage(index + 1);
                    }else{
                        if(!secondActivityStarted){
                            Intent intent = new Intent(getApplicationContext(), ListActivity.class);
                            startActivity(intent);
                            secondActivityStarted = true;
                        }
                    }

                }
            });
            queue.add(request);
        }
    }
}
