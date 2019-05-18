package com.example.pplab3b;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {Student.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {
    public abstract StudentDao studentDao();

}

