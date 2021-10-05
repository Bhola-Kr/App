package com.dreammedia.dreammedia.customWigits.viewPager;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import java.util.ArrayList;
import java.util.List;

public class SliderPagerAdapterStatus extends FragmentStatePagerAdapter {

    private static final String TAG = "SliderPagerAdapter";
    List<Fragment> mFrags = new ArrayList<>();

    public SliderPagerAdapterStatus(FragmentManager fm, List<Fragment> frags) {
        super(fm);
        mFrags = frags;
    }

    @Override
    public Fragment getItem(int position) {
        int index = position % mFrags.size();
        return FragmentSliderStatus.newInstance(
                mFrags.get(index).getArguments().getString("image") ,
                mFrags.get(index).getArguments().getInt("title" ));
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

}