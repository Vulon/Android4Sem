package com.example.lab2;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        FragmentManager fragmentManager = getSupportFragmentManager();
        String tag = "List";
        CustomListFragment listFragment = new CustomListFragment();
        fragmentManager.beginTransaction().replace(R.id.MainLayout,listFragment,tag).commit();


    }
}
