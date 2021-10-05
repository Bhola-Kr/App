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

public class FragmentSlider extends Fragment {

    private String  imageUrls;
    private String  title;
    private String  title1;
    private String  title2;
    private int  desc;
    public FragmentSlider() { }

    public static FragmentSlider newInstance(String drawable , String title, String title1, String title2 , int desc  ) {

        FragmentSlider fragment = new FragmentSlider();
        Bundle args = new Bundle();
        args.putString("image", drawable);
        args.putString("title", title);
        args.putString("title1", title1);
        args.putString("title2", title2);
        args.putInt("desc", desc);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        imageUrls     = getArguments().getString("image");
        title         = getArguments().getString("title");
        title1        = getArguments().getString("title1");
        title2        = getArguments().getString("title2");
        desc          = getArguments().getInt("desc");

        View view       = inflater.inflate(R.layout.fragment_slider_item, container, false);
        ImageView img   = (ImageView) view.findViewById(R.id.img);
        ImageView img1  = (ImageView) view.findViewById(R.id.img1);
        ImageView img2  = (ImageView) view.findViewById(R.id.img2);

        Log.e("addFrag", "onCreateView: " + "addFrag" );

        Glide.with(getActivity()).load(title)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
             // .apply(new RequestOptions().override(600, 200))
                .into(img);

        Glide.with(getActivity()).load(title1)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
             // .apply(new RequestOptions().override(600, 200))
                .into(img1);

        Glide.with(getActivity()).load(title2)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
             // .apply(new RequestOptions().override(600, 200))
                .into(img2);

        // TextViewCompat.setAutoSizeTextTypeUniformWithConfiguration(tvtitle, 8, 28, 4, TypedValue.COMPLEX_UNIT_SP);
        // TextViewCompat.setAutoSizeTextTypeUniformWithConfiguration(tvdesc, 8, 28, 4, TypedValue.COMPLEX_UNIT_SP);

        // tvtitle.setText(Html.fromHtml(getString(title)));
        // tvdesc.setText(Html.fromHtml(getString(desc)));
        // Glide.with(getActivity()).load(imageUrls).into(img);


        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            Bundle vv = new  Bundle();

            vv.putString("image", title);
            vv.putString("type", "image_template");

            vv.putString("desc", "");
            vv.putString("postID", "");
            vv.putString("videoCount", "");

            Navigation.findNavController(view).navigate(R.id.DetailFragment, vv);

        }
        });

        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle vv = new  Bundle();

                vv.putString("image", title1);
                vv.putString("type", "image_template");

                vv.putString("desc", "");
                vv.putString("postID", "");
                vv.putString("videoCount", "");

                Navigation.findNavController(view).navigate(R.id.DetailFragment, vv);

            }
        });

        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle vv = new  Bundle();

                vv.putString("image", title2);
                vv.putString("type", "image_template");

                vv.putString("desc", "");
                vv.putString("postID", "");
                vv.putString("videoCount", "");

                Navigation.findNavController(view).navigate(R.id.DetailFragment, vv);

            }
        });


        return view;
    }
}