package com.example.lab2;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class PageFragment extends Fragment {
    public static int relOrdinalNumber;
    int adapterPageID;
    public void setPageNumber(int pageNumber){
        adapterPageID = pageNumber;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pager_fragment, null);
        TextView name = (TextView)view.findViewById(R.id.nameTextView);
        TextView description = (TextView)view.findViewById(R.id.descriptionTextView);
        ImageView bigIconView = (ImageView)view.findViewById(R.id.bigIconView);
        bigIconView.setScaleType(ImageView.ScaleType.FIT_XY);
        int pageNumber = adapterPageID;
        TechData data = DataManager.list.get(pageNumber);
        name.setText(data.name);
        Log.i("Fragment loaded", data.name);
        description.setText(data.description);
        if(DataManager.list.get(pageNumber).smallImage != null){
            bigIconView.setImageBitmap(DataManager.list.get(pageNumber).smallImage);
        }else{
            Log.i("Image Null", DataManager.list.get(pageNumber).name);
        }
        return view;
    }


}
