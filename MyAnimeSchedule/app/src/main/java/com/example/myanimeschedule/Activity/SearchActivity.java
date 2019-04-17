package com.example.myanimeschedule.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.myanimeschedule.Adapters.SearchListAdapter;
import com.example.myanimeschedule.R;
import com.example.myanimeschedule.Utills.HTTPMessenger;

import org.json.JSONObject;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
    private final int AppBarMAX = 210;
    private final int AppBarMIN = 66;



    private ImageButton searchButton;
    HTTPMessenger messenger;

    private RelativeLayout topLayout;
    private TextView titleSearchLine;
    private SearchListAdapter adapter;
    private RecyclerView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        messenger = HTTPMessenger.getInstance(getApplicationContext());
        searchButton = (ImageButton)findViewById(R.id.searchButton);
        titleSearchLine = (TextView)findViewById(R.id.StitileSearchLine);

        topLayout = (RelativeLayout)findViewById(R.id.appbar);

        listView = (RecyclerView)findViewById(R.id.searchRecycleView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        listView.setLayoutManager(layoutManager);
        ArrayList<JSONObject> jsonObjects = new ArrayList<>();
        if(savedInstanceState != null){
            ArrayList<String> strings = savedInstanceState.getStringArrayList("JSON ARRAY");
            for(int i = 0; i < strings.size(); i++){
                try{
                    jsonObjects.add(new JSONObject(strings.get(i)));
                }catch (Exception e){
                    Log.i("SAVED INST LOAD EXC", e.toString());
                }
            }
            titleSearchLine.setText(savedInstanceState.getString("SearchLine"));
        }
        adapter = new SearchListAdapter(jsonObjects, this);
        listView.setAdapter(adapter);



        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(titleSearchLine.getText().length() > 0){
                    Log.i("ON CLICK","CLICKED");
                    messenger.searchSeries(titleSearchLine.getText().toString(), adapter);
                }
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        ArrayList<String> jsonArray = new ArrayList<>();
        for(int i = 0; i < adapter.jsons.size(); i++){
            jsonArray.add(adapter.jsons.get(i).toString());
        }
        outState.putStringArrayList("JSON ARRAY", jsonArray);
        outState.putString("SearchLine", titleSearchLine.getText().toString());
        super.onSaveInstanceState(outState);
    }
}
