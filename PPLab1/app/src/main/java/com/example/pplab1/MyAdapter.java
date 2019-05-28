package com.example.pplab1;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.net.URL;
import java.util.List;


public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private URL url;
    private String[] dataSet;
    private TypedArray images;
    public class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;
        public TextView textView;
        public MyViewHolder(View viewItem){
            super(viewItem);
            imageView = (ImageView)viewItem.findViewById(R.id.itemImage);
            textView = (TextView)viewItem.findViewById(R.id.itemText);

        }
    }
    public MyAdapter (String[] data, TypedArray images){
        dataSet = data;
        this.images = images;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.item_contact, viewGroup, false);
        MyViewHolder holder = new MyViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        TextView text = myViewHolder.textView;
        text.setText(Translator.parseInt(1000000 - i));

        ImageView image = myViewHolder.imageView;
        int choice = (int) (Math.random() * images.length());
        image.setImageResource(images.getResourceId(choice, R.drawable.android));

        if(i % 2 == 0){
            text.setBackgroundColor(Color.WHITE);
        }else{
            text.setBackgroundColor(Color.GRAY);
        }


    }

    @Override
    public int getItemCount() {
        return dataSet.length;
    }
}
