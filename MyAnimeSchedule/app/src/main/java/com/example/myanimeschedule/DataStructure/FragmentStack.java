package com.example.myanimeschedule.DataStructure;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.example.myanimeschedule.Fragments.CustomHolderFragment;
import com.example.myanimeschedule.Fragments.NotificationsListFragment;
import com.example.myanimeschedule.Fragments.ScheduleFragment;
import com.example.myanimeschedule.Fragments.SeriesListFragment;
import com.example.myanimeschedule.R;
import com.example.myanimeschedule.Utills.NotificationsScheduler;

public class FragmentStack {
    static FragmentStack link = new FragmentStack();
    public FragmentID currentFragmentID;
    boolean startedFirstTime = true;




    public FragmentID previousFragmentID;
    @IdRes int mainLayout = R.id.EmptyLayout;
    Bundle currentBundle;

    private FragmentStack(){
        startedFirstTime = true;
    }
    public static FragmentStack getInstance(){
        return link;
    }

    public void goBack(FragmentManager fragmentManager){

        replaceFragment(previousFragmentID, fragmentManager, null);
    }

    public void replaceFragment(FragmentID fragmentID, FragmentManager fragmentManager, @Nullable Bundle bundle){
        startedFirstTime = false;
        switch (fragmentID){
            case Details:
                CustomHolderFragment customHolderFragment = new CustomHolderFragment();
                customHolderFragment.setArguments(bundle);
                previousFragmentID = currentFragmentID;
                currentFragmentID = fragmentID;
                currentBundle = bundle;
                fragmentManager.beginTransaction().replace(mainLayout, customHolderFragment, fragmentID.toString()).commit();
                break;
            case SeriesList:
                SeriesListFragment seriesListFragment = new SeriesListFragment();
                previousFragmentID = currentFragmentID;
                currentFragmentID = fragmentID;
                fragmentManager.beginTransaction().replace(mainLayout, seriesListFragment, fragmentID.toString()).commit();
                break;
            case Schedule:
                ScheduleFragment scheduleFragment = new ScheduleFragment();
                previousFragmentID = currentFragmentID;
                currentFragmentID = fragmentID;
                fragmentManager.beginTransaction().replace(mainLayout, scheduleFragment, fragmentID.toString()).commit();
                break;
            case NotificationsList:
                NotificationsListFragment notificationsListFragment = new NotificationsListFragment();
                previousFragmentID = currentFragmentID;
                currentFragmentID = fragmentID;
                fragmentManager.beginTransaction().replace(mainLayout, notificationsListFragment, currentFragmentID.toString()).commit();
                break;
        }
    }


    public void restartFragment(FragmentManager fragmentManager){
        if(startedFirstTime){
            currentFragmentID = FragmentID.SeriesList;
            SeriesListFragment seriesListFragment = new SeriesListFragment();
            fragmentManager.beginTransaction().replace(mainLayout, seriesListFragment, currentFragmentID.toString()).commit();
        }else{
            startedFirstTime = false;
            switch (currentFragmentID){
                case Details:
                    CustomHolderFragment pagerHolderFragment = new CustomHolderFragment();
                    pagerHolderFragment.setArguments(currentBundle);
                    fragmentManager.beginTransaction().replace(mainLayout, pagerHolderFragment, currentFragmentID.toString())
                            .commit();
                    break;
                case SeriesList:
                    SeriesListFragment seriesListFragment = new SeriesListFragment();
                    fragmentManager.beginTransaction().replace(mainLayout, seriesListFragment, currentFragmentID.toString()).commit();
                    break;
                case Schedule:
                    ScheduleFragment scheduleFragment = new ScheduleFragment();
                    fragmentManager.beginTransaction().replace(mainLayout, scheduleFragment, currentFragmentID.toString()).commit();
                    break;
                case NotificationsList:
                    NotificationsListFragment notificationsListFragment = new NotificationsListFragment();
                    fragmentManager.beginTransaction().replace(mainLayout, notificationsListFragment, currentFragmentID.toString()).commit();
                    break;
            }
        }
    }






    public enum FragmentID{
        SeriesList, NotificationsList, Schedule, Details
    }
}
