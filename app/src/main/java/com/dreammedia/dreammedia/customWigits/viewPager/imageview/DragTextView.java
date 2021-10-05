package com.dreammedia.dreammedia.customWigits.viewPager.imageview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;

public class DragTextView extends androidx.appcompat.widget.AppCompatEditText {

    protected int mXDelta;
    protected int mYDelta;

    public DragTextView(Context context) {
        this(context, (AttributeSet)null);
    }

    public DragTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public boolean onTouchEvent(MotionEvent event) {
        MarginLayoutParams layoutParams = (MarginLayoutParams)this.getLayoutParams();
        switch(event.getAction() & 255) {
        case 0:
            this.mXDelta = (int)event.getRawX() - layoutParams.leftMargin;
            this.mYDelta = (int)event.getRawY() - layoutParams.topMargin;
            break;
        case 2:
            int[] margins = this.parseMargin(event, (ViewGroup)this.getParent());
            layoutParams.leftMargin = margins[0];
            layoutParams.topMargin = margins[1];
            this.setLayoutParams(layoutParams);
        }

        this.bringToFront();
        return true;
    }

    protected int[] parseMargin(MotionEvent event, ViewGroup parent) {
        int leftMargin = (int)event.getRawX() - this.mXDelta;
        int maxHorizontalMargin = parent.getWidth() - parent.getPaddingLeft() - parent.getPaddingRight() - this.getWidth();
        if (leftMargin <= 0) {
            leftMargin = 0;
        } else if (leftMargin >= maxHorizontalMargin) {
            leftMargin = maxHorizontalMargin;
        }

        int topMargin = (int)event.getRawY() - this.mYDelta;
        int maxVerticalMargin = parent.getHeight() - parent.getPaddingTop() - parent.getPaddingBottom() - this.getHeight();
        if (topMargin <= 0) {
            topMargin = 0;
        } else if (topMargin >= maxVerticalMargin) {
            topMargin = maxVerticalMargin;
        }

        return new int[]{leftMargin, topMargin};
    }
}
