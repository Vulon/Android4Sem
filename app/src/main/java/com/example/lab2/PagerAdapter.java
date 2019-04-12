package com.example.lab2;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

public class PagerAdapter extends FragmentPagerAdapter {
    int selectedPageId;
    public void setPageID(int id){
        selectedPageId = id;
    }


    public PagerAdapter(FragmentManager fm) {
        super(fm);
        selectedPageId = DataManager.pageSelected;

    }
    @Override
    public Fragment getItem(int i) {
        Log.i("ADAPTER FRAGMENT GET", Integer.toString(i));
        PageFragment fragment = new PageFragment();
        fragment.setPageNumber(i);
        return  fragment;
    }

    @Override
    public int getCount() {
        return DataManager.size();
    }



}
