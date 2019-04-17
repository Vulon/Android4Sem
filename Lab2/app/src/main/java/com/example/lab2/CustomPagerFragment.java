package com.example.lab2;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CustomPagerFragment extends Fragment {
    ViewPager pager;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_pager, container, false);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        int index = getArguments().getInt("index");
        pager = getActivity().findViewById(R.id.pager);
        pager.setAdapter(new CustomPagerAdapter(getActivity().getSupportFragmentManager(),getContext()));
        pager.setCurrentItem(index);
    }
}
