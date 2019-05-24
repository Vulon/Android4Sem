package com.example.pplab3b;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    boolean isReady = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button firstButton = findViewById(R.id.firstButton);
        Button secondButton = findViewById(R.id.secondButton);
        Button thirdButton = findViewById(R.id.thirdButton);
        final AppDatabase appDatabase = MyDataHolder.getDatabase(getApplicationContext());
        isReady = false;
        new Thread(() -> {
            appDatabase.studentDao().deleteAll();
            for(int i = 0; i < 5; i++){
                appDatabase.studentDao().insert(MyDataHolder.generateStudent(i));
            }
            MyDataHolder.studentList = appDatabase.studentDao().getAll();
            isReady = true;
        }).start();

        firstButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isReady){
                    Intent intent = new Intent(getApplicationContext(), TableList.class);
                    startActivity(intent);
                }
            }
        });
        secondButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isReady){
                    isReady = false;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            appDatabase.studentDao().insert(MyDataHolder.generateStudent(MyDataHolder.studentList.size()));
                            MyDataHolder.studentList = appDatabase.studentDao().getAll();
                            isReady = true;
                        }
                    }).start();
                }
            }
        });
        thirdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isReady){
                    isReady = false;
                    Log.i("UPDATE BUTTON", Integer.toString(MyDataHolder.studentList.size() - 1));
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            appDatabase.studentDao().updateById(MyDataHolder.studentList.size() - 1);
                            MyDataHolder.studentList = appDatabase.studentDao().getAll();
                            isReady = true;
                        }
                    }).start();
                }
            }
        });


    }
}

