package com.example.lab2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {


    Context context;
    FragmentManager fragmentManager;

    public MyAdapter(Context context, FragmentManager manager) {
        this.context = context;
        fragmentManager = manager;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView iconView;
        public TextView nameTextView;
        public MyViewHolder(View viewItem){
            super(viewItem);
            iconView = (ImageView)viewItem.findViewById(R.id.listItemIcon);
            nameTextView = (TextView)viewItem.findViewById(R.id.listItemName);
        }
        public void addListener(){
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    CustomPagerFragment pagerFragment = new CustomPagerFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt("index", getAdapterPosition());
                    pagerFragment.setArguments(bundle);
                    String tag = "Pager";

                    fragmentManager.beginTransaction().replace(R.id.MainLayout, pagerFragment, tag)
                            .addToBackStack(null)
                            .commit();

                    return true;
                }
            });
        }
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.list_item, viewGroup, false);
        MyViewHolder holder = new MyViewHolder(itemView);
        holder.addListener();
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        TextView name = myViewHolder.nameTextView;
        ImageView imageView = myViewHolder.iconView;
        Log.i("ON BIND", Integer.toString(i));
        TechData data = DataManager.list.get(i);
        name.setText(data.name);
        if(data.isSmallImageLoaded) {
            imageView.setImageBitmap(data.smallImage);
        }
    }

    @Override
    public int getItemCount() {
        return DataManager.size();
    }
}
