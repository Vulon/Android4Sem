package com.example.pplab3;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "student")
public class Student {
    @PrimaryKey
    public int ID;
    public String credentials;
    public long timestamp;

}
