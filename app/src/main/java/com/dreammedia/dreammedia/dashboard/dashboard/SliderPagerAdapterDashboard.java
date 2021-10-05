package com.dreammedia.dreammedia.dashboard.dashboard;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class SliderPagerAdapterDashboard extends FragmentStatePagerAdapter {

    private static final String TAG = "SliderPagerAdapterDashboard";
    List<Fragment> mFrags = new ArrayList<>();

    public SliderPagerAdapterDashboard(FragmentManager fm, List<Fragment> frags) {
        super(fm);
        mFrags = frags;
    }

    @Override
    public Fragment getItem(int position) {
        int index = position % mFrags.size();
        return FragmentDashBoardSlider.newInstance(mFrags.get(index).getArguments().getString("image") ,
                                                   mFrags.get(index).getArguments().getString("title" ));
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

}