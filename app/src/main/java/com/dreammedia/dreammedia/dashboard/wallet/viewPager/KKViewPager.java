package com.dreammedia.dreammedia.dashboard.wallet.viewPager;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import androidx.viewpager.widget.ViewPager;

public class KKViewPager extends ViewPager implements ViewPager.PageTransformer {

    public static final String TAG = "KKViewPager";
    private int mPageMargin;
    private boolean animationEnabled=true;
    private boolean fadeEnabled=false;
  //  private  float fadeFactor=0.5f;
  private  float fadeFactor=0.5f;

    public KKViewPager(Context context) {
        this(context, null);
    }

    public KKViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        // clipping should be off on the pager for its children so that they can scale out of bounds.
        setClipChildren(false);
        setClipToPadding(false);
        // to avoid fade effect at the end of the page
        setOverScrollMode(2);
        setPageTransformer(false, this);
        setOffscreenPageLimit(3);
        mPageMargin = dp2px(context.getResources(), 15);
        setPadding(mPageMargin, 15, mPageMargin, 15);
    }

    public int dp2px(Resources resource, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resource.getDisplayMetrics());
    }
    public void setAnimationEnabled(boolean enable) {
        this.animationEnabled = enable;
    }

    public void setFadeEnabled(boolean fadeEnabled) {
        this.fadeEnabled = fadeEnabled;
    }

    public void setFadeFactor(float fadeFactor) {
        this.fadeFactor = fadeFactor;
    }

    @Override
    public void setPageMargin(int marginPixels) {
        mPageMargin = marginPixels;
        setPadding(mPageMargin, mPageMargin, mPageMargin, mPageMargin);
    }

   public static final float MAX_SCALE  = 1.13f;
    public static final float MIN_SCALE = 0.9f;
    public static final float MAX_ALPHA = 1.0f;
    public static final float MIN_ALPHA = 0.5f;

    private boolean alpha = false;
    private boolean scale = true;

    @Override
    public void transformPage(View page, float position) {

        if (position < -1) {
            position = -1;
        } else if (position > 1) {
            position = 1;
        }
        float tempScale = position < 0 ? 1 + position : 1 - position;
        if(scale){
            float slope = (MAX_SCALE - MIN_SCALE) / 1;
            //a formula
            float scaleValue = MIN_SCALE + tempScale * slope;
            page.setScaleX(scaleValue);
            page.setScaleY(scaleValue);
        }
        if(alpha){
            //blurry
            float alope = (MAX_ALPHA - MIN_ALPHA) / 1;
            float alphaValue = MIN_ALPHA + tempScale * alope;
            page.setAlpha(alphaValue);
        }

    }

}
