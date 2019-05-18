package com.example.pplab3b;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class TableList extends AppCompatActivity {
    RecyclerView recyclerView;
    AppDatabase appDatabase;
    StudentDao dao;
    MyRecyclerAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_list);
        recyclerView = findViewById(R.id.recycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        appDatabase = MyDataHolder.getDatabase(getApplicationContext());
        dao = appDatabase.studentDao();
        adapter = new MyRecyclerAdapter(MyDataHolder.studentList);
        recyclerView.setAdapter(adapter);

    }
}
