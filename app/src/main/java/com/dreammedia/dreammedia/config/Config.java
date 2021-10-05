package com.dreammedia.dreammedia.config;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.Nullable;

public class Config {


    private static SharedPreferences preferences;
    private static SharedPreferences.Editor editor;
    private static final String PREFERENCES = "DreamMedia";

    public static void init(Context context) {
        preferences = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        editor = preferences.edit();

    }

    //save
    public static void savePreferences() {
        editor.commit();
    }

    public static void clearPreferences() {
        editor.clear();
        savePreferences();
    }

    public static void setAccessToken(String userToken) {
        editor.putString("accessToken", userToken);
    }

    public static String getAccessToken() {
        return preferences.getString("accessToken", null);
    }

    public static void setisVerified(Boolean isVerified) { editor.putBoolean("isVerified", isVerified); }
    public static Boolean getisVerified() { return preferences.getBoolean("isVerified", false); }

    public static void setisLogin(Boolean isLogin) { editor.putBoolean("isLogin", isLogin); }
    public static Boolean getisLogin() { return preferences.getBoolean("isLogin", false); }

    public static void isWhoFollow(Boolean whoFollow) { editor.putBoolean("whoFollow", whoFollow); }
    public static Boolean getWhoFollow() { return preferences.getBoolean("whoFollow", false); }

    public static void setUserProfile(String unit) { editor.putString("profile", unit); }
    public static String getUserProfile() { return preferences.getString("profile", null); }

    public static void setUserCover(String unit) { editor.putString("cover", unit); }
    public static String getUserCover() { return preferences.getString("cover", null); }

    public static void setUserEmail(@Nullable String email) { editor.putString("userEmail", email); }
    public static String getUserEmail() {
        return preferences.getString("userEmail", null);
    }

    public static void setUserPhoneNumber(String phone) {
        editor.putString("phone", phone);
    }
    public static String getUserPhoneNumber(){
        return preferences.getString("phone", null);
    }

    public static void setUserName(String phone) {
        editor.putString("userName", phone);
    }
    public static String getUserName(){ return preferences.getString("userName", null); }

    public static void setName(String phone) {
        editor.putString("name", phone);
    }
    public static String getName(){ return preferences.getString("name", null); }

    public static void setUserID(String phone) { editor.putString("setUserID", phone); }
    public static String getUserID(){ return preferences.getString("setUserID", null); }

    public static void setFollowing(String following) { editor.putString("following", following); }
    public static String geFollowing(){ return preferences.getString("following", null); }

    public static void setFollowrs(String following) { editor.putString("followrsg", following); }
    public static String geFollowrs(){ return preferences.getString("followrsg", null); }

    public static void setSlider(String following) { editor.putString("slider", following); }
    public static String getSlider(){ return preferences.getString("slider", null); }

    public static void setPost(String following) { editor.putString("post", following); }
    public static String getPost(){ return preferences.getString("post", null); }

    public static void setRefralCode(String following) { editor.putString("refralcode", following); }
    public static String getRefralCode(){ return preferences.getString("refralcode", null); }

    public static void setCheck(String following) { editor.putString("check", following); }
    public static String getCheck(){ return preferences.getString("check", null); }



}
