package com.example.pplab3b;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import java.util.Date;
import java.util.List;
import java.util.Random;

public class MyDataHolder {
    private static AppDatabase database = null;
    private static String[] names;
    private static String[] surnames;
    private static String[] patronymic;
    static Migration MIGRATION_1_2 = null;



    public static List<Student> studentList;

    public static AppDatabase getDatabase(Context context){
        if(database == null){
            MIGRATION_1_2 = new Migration(1, 2) {
                @Override
                public void migrate(@NonNull SupportSQLiteDatabase database) {
                    database.execSQL("CREATE TABLE 'student_new' ('ID' INTEGER NOT NULL, 'surname' TEXT"
                            + ", 'name' TEXT, patronymic TEXT, 'timestamp' INTEGER NOT NULL, PRIMARY KEY('ID'))");
                    database.execSQL("INSERT INTO student_new (ID, name, surname, patronymic, timestamp) "+
                            " SELECT ID, substr(credentials, 1, pos - 1), substr(credentials, pos + 1), '', timestamp FROM"+
                            "(SELECT *, instr(credentials, ' ') AS pos FROM student)");
                    database.execSQL("UPDATE student_new SET patronymic = substr(surname, instr(surname, ' ') + 1) ");
                    database.execSQL("UPDATE student_new SET surname = substr(surname, 1, instr(surname, ' '))");
                    database.execSQL("DROP TABLE student");
                    database.execSQL("ALTER TABLE student_new RENAME TO student");
                }
            };
            database = Room.databaseBuilder(context, AppDatabase.class,
                    "firstdatabase").addMigrations(MIGRATION_1_2).build();


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

        student.surname = surnames[random.nextInt(surnames.length)] + " ";
        student.name = names[random.nextInt(names.length)] + " ";
        student.patronymic = patronymic[random.nextInt(patronymic.length)];

        student.timestamp = (new Date()).getTime();
        student.ID = id;
        return student;
    }

}
