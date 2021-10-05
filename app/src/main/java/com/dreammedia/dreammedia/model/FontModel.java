package com.dreammedia.dreammedia.model;

import android.graphics.Typeface;

public class FontModel {

    Typeface face ;
    Boolean mSelectd;

    public Boolean getmSelectd() {
        return mSelectd;
    }

    public void setmSelectd(Boolean mSelectd) {
        this.mSelectd = mSelectd;
    }

    public Typeface getFace() { return face; }
    public void setFace(Typeface face) { this.face = face; }

    public FontModel(Typeface face , Boolean isSelectd) {
        this.face      = face;
        this.mSelectd   = isSelectd;
    }

}
