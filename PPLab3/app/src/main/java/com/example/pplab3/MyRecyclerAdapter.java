package com.example.pplab3;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.StudentHolder>{
    ArrayList<Student> students;

    public MyRecyclerAdapter(List<Student> students) {
        this.students = new ArrayList<>();
        this.students.addAll(students);
    }

    @NonNull
    @Override
    public StudentHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.old_student_item, viewGroup, false);
        StudentHolder holder = new StudentHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull StudentHolder studentHolder, int i) {
        studentHolder.idTextView.setText(Integer.toString(students.get(i).ID));
        studentHolder.fioTextView.setText(students.get(i).credentials);
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTimeInMillis(students.get(i).timestamp);
        String line = Integer.toString(calendar.get(Calendar.DAY_OF_MONTH)) + ".";
        line += Integer.toString(calendar.get(Calendar.MONTH)) + ".";
        line += Integer.toString(calendar.get(Calendar.YEAR)) + " ";
        line += Integer.toString(calendar.get(Calendar.HOUR_OF_DAY)) + ":";
        line += Integer.toString(calendar.get(Calendar.MINUTE));
        studentHolder.dateTextView.setText(line);
    }

    @Override
    public int getItemCount() {
        return students.size();
    }

    class StudentHolder extends RecyclerView.ViewHolder{
        TextView idTextView;
        TextView fioTextView;
        TextView dateTextView;

        public StudentHolder(@NonNull View itemView) {
            super(itemView);
            idTextView = itemView.findViewById(R.id.itemID);
            fioTextView = itemView.findViewById(R.id.itemFIO);
            dateTextView = itemView.findViewById(R.id.itemDate);

        }
    }
}
