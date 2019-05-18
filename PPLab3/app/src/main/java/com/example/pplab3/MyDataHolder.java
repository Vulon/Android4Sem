package com.example.pplab3;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class MyDataHolder {
    private static AppDatabase database = null;
    private static String[] names;
    private static String[] surnames;
    private static String[] patronymic;

    public static List<Student> studentList;

    public static AppDatabase getDatabase(Context context){
        if(database == null){
            database = Room.databaseBuilder(context, AppDatabase.class,
                    "firstdatabase").build();

        }
        Resources resources = context.getResources();
        names = resources.getStringArray(R.array.names);
        surnames = resources.getStringArray(R.array.surnames);
        patronymic = resources.getStringArray(R.array.patronymic);

        return database;
    }
    public static void deleteDataBase(){
        if(database == null){
            return;
        }
        database.studentDao().deleteAll();
    }

    public static Student generateStudent(int id){

        Student student = new Student();
        Random random = new Random((new Date()).getTime());
        String line = "";
        line += surnames[random.nextInt(surnames.length)] + " ";
        line += names[random.nextInt(names.length)] + " ";
        line += patronymic[random.nextInt(patronymic.length)];
        Log.i("STUDENT GENERATOR", line);
        student.credentials = line;
        student.timestamp = (new Date()).getTime();
        student.ID = id;
        return student;
    }

}
