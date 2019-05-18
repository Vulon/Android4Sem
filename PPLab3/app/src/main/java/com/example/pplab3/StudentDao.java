package com.example.pplab3;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface StudentDao {

    @Query("SELECT * FROM student")
    List<Student> getAll();

    @Insert
    void insert(Student student);
    @Update
    void update(Student student);
    @Delete
    void delete(Student student);
    @Query("DELETE FROM student")
    void deleteAll();
    @Query("UPDATE student SET credentials = 'Иванов Иван Иванович' WHERE ID = :studentid")
    void updateById(int studentid);
}
