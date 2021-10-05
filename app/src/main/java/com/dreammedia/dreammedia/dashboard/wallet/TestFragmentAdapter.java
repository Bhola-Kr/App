package com.dreammedia.dreammedia.dashboard.wallet;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.dreammedia.dreammedia.model.WalletResponse;

import java.util.ArrayList;
import java.util.List;


class TestFragmentAdapter extends FragmentStatePagerAdapter {

    private Context context;
    List<WalletResponse.Responce>  list = new ArrayList<>();



    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        Object obj = super.instantiateItem(container, position);
        return obj;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        if (object != null) {
            return ((Fragment) object).getView() == view;
        } else {
            return false;
        }
    }

    public TestFragmentAdapter(FragmentManager fm, Context context,  List<WalletResponse.Responce>  mlist) {
        super(fm);
        this.context = context;
        list = mlist;
    }

    @Override
    public int getItemPosition(Object object) {
        // Causes adapter to reload all Fragments when
        // notifyDataSetChanged is called
        return POSITION_NONE;
    }

    @Override
    public Fragment getItem(int position) {
        return PriseCartFragment.newInstance(list.get(position), context);

    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }



}