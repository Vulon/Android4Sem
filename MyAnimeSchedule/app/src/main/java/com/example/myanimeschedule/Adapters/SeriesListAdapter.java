package com.example.myanimeschedule.Adapters;

import android.content.Context;
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
import com.example.myanimeschedule.DataStructure.FragmentStack;
import com.example.myanimeschedule.DataStructure.SubscriptionsManager;
import com.example.myanimeschedule.R;

public class SeriesListAdapter extends RecyclerView.Adapter<SeriesListAdapter.SLViewHolder>{
    SubscriptionsManager manager;
    Context context;
    FragmentManager fragmentManager;

    public SeriesListAdapter(Context context, FragmentManager fragmentManager) {
        this.context = context;
        this.fragmentManager = fragmentManager;
        manager = SubscriptionsManager.getInstance(context);
    }



    @NonNull
    @Override
    public SLViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, final int i) {
        final Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        final View itemView = inflater.inflate(R.layout.subscriptions_list_layout, viewGroup, false);

        final SLViewHolder holder = new SLViewHolder(itemView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("seriesID", manager.seriesData.get(holder.getAdapterPosition()).id);
                FragmentStack fragmentStack = FragmentStack.getInstance();
                fragmentStack.replaceFragment(FragmentStack.FragmentID.Details, fragmentManager, bundle);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull SLViewHolder slViewHolder, int i) {
        SubscriptionsManager.SeriesData seriesData = manager.seriesData.get(i);
        slViewHolder.bannerView.setImageBitmap(seriesData.banner.getBitmap());
        slViewHolder.bannerView.setMaxHeight(slViewHolder.bannerView.getWidth() / 5);
        slViewHolder.bannerView.setMinimumHeight(slViewHolder.bannerView.getWidth() / 5);
        slViewHolder.nameView.setText(seriesData.name);
        slViewHolder.statusView.setText("Status: " + seriesData.status);
        slViewHolder.seasonsCountView.setText("Sesons: " + Integer.toString(seriesData.getLastSeason()));
        slViewHolder.seriesOrdinal = i;
        if(seriesData.getLastEpisode() != null){
            slViewHolder.lastSeasonEpisodesView.setText("Last episode number: " + Integer.toString(seriesData.getLastEpisode().episodeNumber));
            slViewHolder.lastEpisodeAirDate.setText("Last aired: " + seriesData.getLastEpisode().firstAired);
        }else{
            slViewHolder.lastSeasonEpisodesView.setText("Last episode number: " + "unknown");
            slViewHolder.lastEpisodeAirDate.setText("unknown");
        }
    }

    @Override
    public int getItemCount() {
        return manager.seriesData.size();
    }

    class SLViewHolder extends RecyclerView.ViewHolder{
        ImageView bannerView;
        TextView nameView;
        TextView statusView;
        TextView seasonsCountView;
        TextView lastSeasonEpisodesView;
        TextView lastEpisodeAirDate;
        int seriesOrdinal;
        public SLViewHolder(View viewItem){
            super(viewItem);
            bannerView = viewItem.findViewById(R.id.SLbannerView);
            nameView = viewItem.findViewById(R.id.SLname);
            statusView = viewItem.findViewById(R.id.SLstatus);
            seasonsCountView = viewItem.findViewById(R.id.SLseasonCount);
            lastSeasonEpisodesView = viewItem.findViewById(R.id.SLlastSeasonEpisodes);
            lastEpisodeAirDate = viewItem.findViewById(R.id.SLlastEpisodeAirDate);
        }
    }
}
