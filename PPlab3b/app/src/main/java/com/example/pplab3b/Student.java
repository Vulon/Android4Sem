package com.example.pplab3b;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "student")
public class Student {
    @PrimaryKey
    public int ID;
    public String name;
    public String surname;
    public String patronymic;
    public long timestamp;

}
