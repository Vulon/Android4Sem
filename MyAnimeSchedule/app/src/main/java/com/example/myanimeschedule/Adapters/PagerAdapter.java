package com.example.myanimeschedule.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.myanimeschedule.DataStructure.PageFragment;

public class PagerAdapter extends FragmentPagerAdapter {
    private int seriesID;
    @Override
    public Fragment getItem(int i) {
        PageFragment fragment = new PageFragment();
        fragment.setAdapterPosition(i, seriesID);
        return fragment;
    }
    public PagerAdapter(FragmentManager fm, int seriesID) {
        super(fm);
        this.seriesID = seriesID;
    }

    @Override
    public int getCount() {
        return 3;
    }


}
