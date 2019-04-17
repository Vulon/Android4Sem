package com.example.myanimeschedule.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.myanimeschedule.Activity.SeriesInfoActivity;
import com.example.myanimeschedule.DataStructure.SubscriptionsManager;
import com.example.myanimeschedule.R;

import org.json.JSONObject;

import java.util.ArrayList;

public class EpisodesAdapter extends RecyclerView.Adapter<EpisodesAdapter.EpisodeHolder>{
    private Context context;
    static SubscriptionsManager.SeriesData data;
    public EpisodesAdapter(Context context, SubscriptionsManager.SeriesData seriesData) {
        this.context = context;
        data = seriesData;
    }

    public class EpisodeHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView number;
        public TextView firstAired;
        public TextView overview;
        public RelativeLayout layout;
        public EpisodeHolder(View viewItem){
            super(viewItem);
            name = viewItem.findViewById(R.id.ELIname);
            number = viewItem.findViewById(R.id.ELInumber);
            firstAired = viewItem.findViewById(R.id.ELIfirsrtAired);
            overview = viewItem.findViewById(R.id.ELIoverview);
            layout = viewItem.findViewById(R.id.ELIlayout);
            overview.setVisibility(View.INVISIBLE);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) layout.getLayoutParams();
            params.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80, itemView.getResources().getDisplayMetrics());
            layout.setLayoutParams(params);
            setOnClick();
        }
        void setOnClick(){
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(overview.getVisibility()  == View.VISIBLE){
                        overview.setVisibility(View.INVISIBLE);
                        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) layout.getLayoutParams();
                        params.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80, itemView.getResources().getDisplayMetrics());
                        layout.setLayoutParams(params);
                    }else{
                        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) layout.getLayoutParams();
                        params.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
                        layout.setLayoutParams(params);
                        overview.setVisibility(View.VISIBLE);
                    }
                }
            });
        }

    }



    @NonNull
    @Override
    public EpisodesAdapter.EpisodeHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        final Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.recycler_episodes_item, viewGroup, false);

        EpisodesAdapter.EpisodeHolder holder = new EpisodesAdapter.EpisodeHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull EpisodeHolder episodeHolder, int i) {
        SubscriptionsManager.SeriesData.Episode episode = data.episodes.get(i);

        episodeHolder.name.setText(episode.episodeName);
        episodeHolder.number.setText("S: " + Integer.toString(episode.seasonsNumber) + " E: " + Integer.toString(episode.episodeNumber));
        episodeHolder.firstAired.setText("Aired: " + episode.firstAired);
        episodeHolder.overview.setText(episode.episodeOverView);
    }



    @Override
    public int getItemCount() {
        return data.episodes.size();
    }
}
