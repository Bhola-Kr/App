package com.dreammedia.dreammedia.customWigits.viewPager;

import android.util.Log;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import java.util.ArrayList;
import java.util.List;

public class SliderPagerAdapter extends FragmentStatePagerAdapter {

    private static final String TAG = "SliderPagerAdapter";
    List<Fragment> mFrags = new ArrayList<>();

    public SliderPagerAdapter(FragmentManager fm, List<Fragment> frags) {
        super(fm);
        mFrags = frags;
    }

    @Override
    public Fragment getItem(int position) {
        int index = position % mFrags.size();
        Log.d(TAG, "position: " + position);
        Log.d(TAG, "index: " + index);
        Log.e(TAG, "getItem: "+ mFrags.get(index).getArguments().getString("params"));

        return FragmentSlider.newInstance(
                mFrags.get(index).getArguments().getString("image") ,
                mFrags.get(index).getArguments().getString("title" ),
                mFrags.get(index).getArguments().getString("title1"),
                mFrags.get(index).getArguments().getString("title2"),
                mFrags.get(index).getArguments().getInt("desc"));
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

}