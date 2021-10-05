package com.dreammedia.dreammedia.config;

import android.app.Application;
import android.content.Context;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;

import okhttp3.OkHttpClient;

public class App extends Application {

    public static Context APP_CONTEXT;
    private static OkHttpClient client = null;
    private static App instance;
    private GoogleSignInClient googleSignInClient;


    public static OkHttpClient getClient() {
        client = new OkHttpClient();
        return client;
    }

    public static synchronized App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        APP_CONTEXT = getApplicationContext();
    }

    public GoogleSignInClient getGoogleSignInClient() {
        if (googleSignInClient != null)
            return googleSignInClient;
        return null;
    }


    public void setGoogleSignInClient(GoogleSignInClient googleSignInClient) { this.googleSignInClient = googleSignInClient; }

   /* fun logOut(mContext: Context) {
        *//*To Logout from facebook*//*
        LoginManager.getInstance().logOut()
        VagoPreference(mContext).clearPreferences(mContext)

        *//*To Logout From Google SignIn*//*
        try {
            if (VagoApp.getInstance().googleSignInClient != null) {
                VagoApp.getInstance().googleSignInClient.signOut()
                VagoApp.getInstance().googleSignInClient.asGoogleApiClient().clearDefaultAccountAndReconnect()
                VagoApp.getInstance().googleSignInClient.asGoogleApiClient().disconnect()
                VagoApp.getInstance().googleSignInClient.asGoogleApiClient().connect()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }*/

}

