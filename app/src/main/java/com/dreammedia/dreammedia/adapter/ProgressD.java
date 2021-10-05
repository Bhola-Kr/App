package com.dreammedia.dreammedia.adapter;

import android.app.ProgressDialog;
import android.content.Context;

public class ProgressD {

    public void myProgress(Context context,String message,boolean show){

        ProgressDialog progress=new ProgressDialog(context);
        progress.setMessage(""+message);
        progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progress.setIndeterminate(true);
        progress.setProgress(0);
        if (show==true){
            progress.show();
        }else {
            progress.hide();
        }


    }



}
