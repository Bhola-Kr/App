package com.dreammedia.dreammedia.utlis;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InstaAccessToken {

    private String access_token;
    private String user_id;

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}