package com.example.myanimeschedule.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myanimeschedule.Activity.SeriesInfoActivity;
import com.example.myanimeschedule.R;

import org.json.JSONObject;
import java.util.ArrayList;

public class SearchListAdapter extends RecyclerView.Adapter<SearchListAdapter.MyViewHolder> {
    public ArrayList<JSONObject> jsons;
    private boolean hasData = false;
    private Context context;
    public SearchListAdapter(ArrayList<JSONObject> jsons, Context context) {
        this.jsons = jsons;
        this.context = context;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView network;
        public TextView publishDate;
        public TextView status;
        public MyViewHolder(View viewItem){
            super(viewItem);
            name = (TextView)viewItem.findViewById(R.id.seriesName);
            network = (TextView)viewItem.findViewById(R.id.seriesNetwork);
            publishDate = (TextView)viewItem.findViewById(R.id.seriesPublishDate);
            status = (TextView)viewItem.findViewById(R.id.seriesStatus);
        }
        public void bind(){
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, SeriesInfoActivity.class);
                    try{
                        intent.putExtra("ID", jsons.get(getAdapterPosition()).getString("id"));
                    }catch (Exception e){
                        Log.e("SEARCH LIST ADAPTER", "Intent put json id field: "+ e.toString());
                    }
                    intent.putExtra("mode", 0);
                    context.startActivity(intent);
                }

            });
        }
    }



    public void loadData(ArrayList<JSONObject> jsonObjects){
        hasData = true;
        jsons = jsonObjects;
        notifyDataSetChanged();
    }
    public void seriesNotFound(){
        hasData = false;
        jsons.clear();
        jsons.add(new JSONObject());

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        final Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.recycler_view_item, viewGroup, false);

        MyViewHolder holder = new MyViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        if(hasData){
            try {
                myViewHolder.name.setText(jsons.get(i).getString("seriesName"));
                myViewHolder.network.setText(jsons.get(i).getString("network"));
                myViewHolder.publishDate.setText(jsons.get(i).getString("firstAired"));
                myViewHolder.status.setText("Status: " +jsons.get(i).getString("status"));
            }catch (org.json.JSONException e) {
                Log.e("SEARCH LIST ADAPTER", "On bind view holder json err " +e.toString());
            }
            myViewHolder.network.setVisibility(View.VISIBLE);
            myViewHolder.publishDate.setVisibility(View.VISIBLE);
            myViewHolder.status.setVisibility(View.VISIBLE);
            myViewHolder.bind();
        }else{
            myViewHolder.network.setVisibility(View.INVISIBLE);
            myViewHolder.publishDate.setVisibility(View.INVISIBLE);
            myViewHolder.status.setVisibility(View.INVISIBLE);
            myViewHolder.name.setText("Could not find series with such name");
        }
    }

    @Override
    public int getItemCount() {
        return jsons.size();
    }
}
