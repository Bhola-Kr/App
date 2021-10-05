package com.dreammedia.dreammedia.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.dreammedia.dreammedia.R;
import com.dreammedia.dreammedia.config.Config;
import com.dreammedia.dreammedia.loginProcess.LoginSignupActivity;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_splash_screen );

//        new Handler().postDelayed( new Runnable() {
//            @Override
//            public void run() {
//                if (Config.getisLogin()) {
//                    startActivity( new Intent( getApplicationContext(), DashboardActivity.class ) );
//                    finish();
//                } else {
//                    Intent i = new Intent( getApplicationContext(), LoginSignupActivity.class );
//                    startActivity( i );
//                    finish();
//                }
//            }
//        }, 4000 );
    }
}