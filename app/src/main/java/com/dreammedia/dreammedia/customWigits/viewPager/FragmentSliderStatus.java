package com.dreammedia.dreammedia.customWigits.viewPager;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dreammedia.dreammedia.R;

public class FragmentSliderStatus extends Fragment {

    private String  imageUrls;
    private int  title;
    public FragmentSliderStatus() { }

    public static FragmentSliderStatus newInstance(String drawable , int title  ) {

        FragmentSliderStatus fragment = new FragmentSliderStatus();
        Bundle args = new Bundle();
        args.putString("image", drawable);
        args.putInt("title", title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        imageUrls       = getArguments().getString("image");
        title           = getArguments().getInt("title");

        View view       = inflater.inflate(R.layout.fragment_slider_item_status, container, false);
        ImageView img   = (ImageView) view.findViewById(R.id.img);

        Glide.with(getContext()).load(imageUrls)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.ic_user_)
                .into(img);


        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle vv = new  Bundle();

                vv.putString("image", imageUrls);
                vv.putString("type", "image_template");

                vv.putString("desc", "");
                vv.putString("postID", "");
                vv.putString("videoCount", "");

                Navigation.findNavController(view).navigate(R.id.DetailFragment, vv);

            }
        });


        Log.e("addFrag", "onCreateView: " + "addFrag" );
        return view;

    }

}