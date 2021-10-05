package com.dreammedia.dreammedia.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PostAddResponse {

    private Boolean status;
    private String message;
    private String responce;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getResponce() {
        return responce;
    }

    public void setResponce(String responce) {
        this.responce = responce;
    }

}