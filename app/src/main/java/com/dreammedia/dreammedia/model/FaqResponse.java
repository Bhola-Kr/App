package com.dreammedia.dreammedia.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FaqResponse {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("responce")
    @Expose
    private List<Responce> responce = null;

    public Boolean getStatus() { return status; }
    public void setStatus(Boolean status) { this.status = status; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public List<Responce> getResponce() { return responce; }
    public void setResponce(List<Responce> responce) { this.responce = responce; }

    public class Responce {

        private String title;
        private String description;

        private boolean Visable;

        public boolean getmVisable() { return Visable; }
        public void setmVisable(Boolean visable) { Visable = visable; }

        public String getTitle() { return title; }
        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }
        public void setDescription(String description) {
            this.description = description;
        }

    }

}

