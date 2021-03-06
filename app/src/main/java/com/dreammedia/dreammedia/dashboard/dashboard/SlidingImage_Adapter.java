package com.dreammedia.dreammedia.dashboard.dashboard;

import android.content.Context;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.dreammedia.dreammedia.R;

import java.util.List;


public class SlidingImage_Adapter extends PagerAdapter {
 
 
    private List<String> IMAGES;
    private LayoutInflater inflater;
    private Context context;
    private String type;

    public SlidingImage_Adapter(Context context , List<String> IMAGES , String type) {
        this.context = context;
        this.IMAGES  = IMAGES;
        this.type    = type;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
 
    @Override
    public int getCount() {
        return IMAGES.size();
    }
 
    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View imageLayout = inflater.inflate(R.layout.fragment_slider_item_dashboard, view, false);

        assert imageLayout != null;
        final ImageView imageView = (ImageView) imageLayout.findViewById(R.id.img);
        final ImageView icVideoPlay = (ImageView) imageLayout.findViewById(R.id.icVideoPlay);

        try {
            if (type.equals("video")){
                icVideoPlay.setVisibility(View.VISIBLE);
            }else{
                icVideoPlay.setVisibility(View.GONE);
            }
        } catch (Exception e) { e.printStackTrace(); }

        Glide.with(context).load(IMAGES.get(position))
                .centerCrop()
                .into(imageView);
 
        view.addView(imageLayout, 0);
        return imageLayout;
    }
 
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }
 
    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }
 
    @Override
    public Parcelable saveState() {
        return null;
    }
 
 
}