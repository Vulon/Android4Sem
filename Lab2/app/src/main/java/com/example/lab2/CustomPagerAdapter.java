package com.example.lab2;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomPagerAdapter extends FragmentStatePagerAdapter {
    private Context context;

    public CustomPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int i) {


        return ArrayListFragment.newInstance(i);
    }
    public static class ArrayListFragment extends Fragment{
        int index;
        static ArrayListFragment newInstance(int num) {
            ArrayListFragment f = new ArrayListFragment();
            Bundle args = new Bundle();
            args.putInt("num", num);
            f.setArguments(args);
            return f;
        }
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            index = getArguments() != null ? getArguments().getInt("num") : 1;
        }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.pager_fragment, container, false);
            TextView name = v.findViewById(R.id.PFname);
            TextView description = v.findViewById(R.id.PFdesription);
            ImageView bigIconView = v.findViewById(R.id.PFicon);
            bigIconView.setScaleType(ImageView.ScaleType.FIT_XY);
            TechData data = DataManager.list.get(index);
            name.setText(data.name);
            description.setText(data.description);
            bigIconView.setImageBitmap(DataManager.list.get(index).smallImage);
            Log.i("NEW FRAGMENT", name.getText().toString());
            return v;
        }
        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
        }

    }
    /*
        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
            return view.equals(o);
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.pager_fragment,null);
            TextView name = view.findViewById(R.id.nameTextView);
            TextView description = view.findViewById(R.id.descriptionTextView);
            ImageView bigIconView = view.findViewById(R.id.bigIconView);
            bigIconView.setScaleType(ImageView.ScaleType.FIT_XY);
            TechData data = DataManager.list.get(position);
            name.setText(data.name);

            description.setText(data.description);
            Log.i("ADAPTER", name.getText().toString());
            if(DataManager.list.get(position).smallImage != null){
                bigIconView.setImageBitmap(DataManager.list.get(position).smallImage);
            }else{
                Log.i("Image Null", DataManager.list.get(position).name);
            }
            return view;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View)object);
        }
        */
    @Override
    public int getCount() {
        return DataManager.size();
    }



}
