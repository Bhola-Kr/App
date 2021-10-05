package com.dreammedia.dreammedia.dashboard.dashboard;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.dreammedia.dreammedia.R;

public class FragmentDashBoardSlider extends Fragment {

    private String  imageUrls;
    private String      title;
    public FragmentDashBoardSlider() { }

    public static FragmentDashBoardSlider newInstance(String drawable , String title ) {

        FragmentDashBoardSlider fragment = new FragmentDashBoardSlider();
        Bundle args = new Bundle();
        args.putString("image", drawable);
        args.putString("title", title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view       = inflater.inflate(R.layout.fragment_slider_item_dashboard, container, false);
        ImageView img   = (ImageView) view.findViewById(R.id.img);

       // imageUrls       = getArguments().getString("image");
        title           = getArguments().getString("title");

        Log.e("addFrag", "onCreateView: " + title );
        Glide.with(getActivity()).load(title)
                .centerCrop()
                .into(img);


        return view;
    }
}