package com.dreammedia.dreammedia.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CategorySliderModel {

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

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("images")
        @Expose
        private List<String> images = null;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public List<String> getImages() {
            return images;
        }

        public void setImages(List<String> images) {
            this.images = images;
        }

    }

}