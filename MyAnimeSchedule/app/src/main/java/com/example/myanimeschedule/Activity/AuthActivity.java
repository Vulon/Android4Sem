package com.example.myanimeschedule.Activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.myanimeschedule.DataStructure.SubscriptionsManager;
import com.example.myanimeschedule.Utills.FileManager;
import com.example.myanimeschedule.Utills.HTTPMessenger;
import com.example.myanimeschedule.DataStructure.ObservableData;
import com.example.myanimeschedule.R;
import com.example.myanimeschedule.Utills.NotificationsScheduler;
import com.example.myanimeschedule.Utills.ScheduleReciever;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Observable;
import java.util.Observer;

public class AuthActivity extends AppCompatActivity {
    public TextView errorLine;
    private LoginObserver observer;
    private TextView username;
    private TextView userkey;
    private Button loginButton;
    private ObservableData observableData;
    private CheckBox checkBox;


    HTTPMessenger messenger;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        errorLine = (TextView)findViewById(R.id.errorLine);
        observer = new LoginObserver();
        observer.setup(errorLine);
        messenger = HTTPMessenger.getInstance(getApplicationContext());
        userkey = (TextView)findViewById(R.id.userkeyText);
        username = (TextView)findViewById(R.id.usernameText);
        loginButton = (Button)findViewById(R.id.loginButton);
        observableData = new ObservableData();
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                observableData.clear();
                observableData.addObserver(observer);
                messenger.login(username.getText().toString(), userkey.getText().toString(), observableData);
            }
        });
        checkBox = (CheckBox)findViewById(R.id.rememberpassBox);
        FileManager manager = new FileManager();
        boolean loaded = manager.loadData(AuthActivity.this);
        if(loaded){
            username.setText(messenger.getUsername());
            userkey.setText(messenger.getUserkey());
            Log.i("AUTH ACTIVITY", "USERNAME: " +messenger.getUsername());
            Log.i("AUTH ACTIVITY", "USERKEY " +messenger.getUserkey());
            checkBox.setChecked(true);
        }
    }

    private class LoginObserver implements Observer{
        private TextView errorLine;
        public void setup(TextView errorLine){
            this.errorLine = errorLine;
        }
        @Override
        public void update(Observable o, Object arg) {
            ObservableData observableData = (ObservableData)o;
            if(observableData.isError){
                errorLine.setVisibility(View.VISIBLE);
                errorLine.setText(observableData.errorMsg);
            }else{
                if(checkBox.isChecked()){
                    FileManager manager = new FileManager();
                    manager.saveData(AuthActivity.this);
                }
                SubscriptionsManager.getInstance(getApplicationContext()).load(getApplicationContext());

                NotificationsScheduler scheduler = NotificationsScheduler.getInstance();
                scheduler.updateEntryList(getApplicationContext());

                scheduler.saveEntriesData(getApplicationContext());
                ScheduleReciever.startServiceUP(getApplicationContext());
                scheduler.loadRecordsData(getApplicationContext());

                startActivity(new Intent(AuthActivity.this, MainActivity.class));
            }
        }
    }
}
