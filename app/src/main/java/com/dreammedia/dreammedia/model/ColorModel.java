package com.dreammedia.dreammedia.model;


public class ColorModel {

    int color ;
    boolean mSelected;

    public boolean getmSelected() {
        return mSelected;
    }

    public void setmSelected(boolean mSelected) {
        this.mSelected = mSelected;
    }

    public int getColor() { return color; }
    public void setColor(int color) { this.color = color; }

    public ColorModel(int color, boolean mselected) {
        this.color = color;
        this.mSelected = mselected;
    }
}

