package com.example.myanimeschedule.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myanimeschedule.Activity.DetailsActivity;
import com.example.myanimeschedule.DataStructure.SubscriptionsManager;
import com.example.myanimeschedule.R;

public class SeriesListAdapter extends RecyclerView.Adapter<SeriesListAdapter.SLViewHolder>{
    SubscriptionsManager manager;
    Context context;

    public SeriesListAdapter(Context context) {
        this.context = context;
        manager = SubscriptionsManager.getInstance(context);
    }



    @NonNull
    @Override
    public SLViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, final int i) {
        final Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.subscriptions_list_layout, viewGroup, false);

        SLViewHolder holder = new SLViewHolder(itemView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailsActivity.class);
                intent.putExtra("SelectedSeriesID", manager.seriesData.get(i).id);
                context.startActivity(intent);
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
        if(seriesData.getLastEpisode() != null){
            Log.i("EPISODE NAME", seriesData.getLastEpisode().episodeName);
            Log.i("EPISODE NUMBER", Integer.toString(seriesData.getLastEpisode().episodeNumber));
            Log.i("SEASOM NUMBER", Integer.toString(seriesData.getLastEpisode().seasonsNumber));
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
