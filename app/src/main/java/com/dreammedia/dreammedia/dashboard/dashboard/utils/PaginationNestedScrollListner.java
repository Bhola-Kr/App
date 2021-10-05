package com.dreammedia.dreammedia.dashboard.dashboard.utils;

import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;

/**
 * Pagination
 * Created by GOVIND SAINI on feb 2021.
 * Copyright (c) 2016. Suleiman Ali Shakir. All rights reserved.
 */
public abstract class PaginationNestedScrollListner implements NestedScrollView.OnScrollChangeListener {


    private LinearLayoutManager layoutManager;

    /**
     * Supporting only LinearLayoutManager for now.
     *
     * @param layoutManager
     */
    protected PaginationNestedScrollListner(LinearLayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }

    public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {


        if ((scrollY >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight())) && scrollY > oldScrollY) {

            int visibleItemCount         = layoutManager.getChildCount();
            int totalItemCount           = layoutManager.getItemCount();
            int pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();


                    if (!isLoading() && !isLastPage()) {

                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount && pastVisiblesItems >= 0) {
                            loadMoreItems();
                        }

                }


        }


       /* view.getChildAt(view.getChildCount() - 1);
        int diff = (view.getBottom() - (view.getHeight() + view.getScrollY()));
        if (diff == 0) {

            int visibleItemCount         = layoutManager.getChildCount();
            int totalItemCount           = layoutManager.getItemCount();
            int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

            if (!isLoading() && !isLastPage()) {
                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0) {
                    loadMoreItems();
                }
            }
        }*/


    }

    protected abstract void loadMoreItems();

    public abstract int getTotalPageCount();

    public abstract boolean isLastPage();

    public abstract boolean isLoading();

}
