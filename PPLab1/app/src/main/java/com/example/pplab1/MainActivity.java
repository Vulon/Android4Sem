package com.example.pplab1;

import android.content.Intent;
import android.os.Debug;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    ImageView imageView;
    boolean sunIsUp = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView)findViewById(R.id.sunImageView);
        imageView.setImageResource(R.drawable.sun);
        //Handler handler = new Handler();

        imageView.postDelayed(new Runnable() {
            @Override
            public void run() {
                changeActivity();
            }
        }, 2000);


    }
    private void changeActivity(){
        startActivity(new Intent(this, ListActivity.class));
    }


}
