package com.dreammedia.dreammedia.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class SearchPostResponse {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("responce")
    @Expose
    private List<Responce> responce = null;

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

    public List<Responce> getResponce() {
        return responce;
    }

    public void setResponce(List<Responce> responce) {
        this.responce = responce;
    }

    public class Responce {

        @SerializedName("post_id")
        @Expose
        private Integer postId;
        @SerializedName("user_id")
        @Expose
        private Integer userId;
        @SerializedName("images")
        @Expose
        private String images;
        @SerializedName("description")
        @Expose
        private String description;
        @SerializedName("is_download")
        @Expose
        private String isDownload;
        @SerializedName("profile_image")
        @Expose
        private String profileImage;
        @SerializedName("fullname")
        @Expose
        private String fullname;
        @SerializedName("total_like")
        @Expose
        private Integer totalLike;
        @SerializedName("total_share")
        @Expose
        private Integer totalShare;
        @SerializedName("post_image")
        @Expose
        private List<String> postImage = null;

        private int is_like ;

        private String added;

        public int getIs_like() { return is_like; }
        public void setIs_like(int is_like) { this.is_like = is_like; }

        public Integer getPostId() {
            return postId;
        }

        public void setPostId(Integer postId) {
            this.postId = postId;
        }

        public Integer getUserId() {
            return userId;
        }

        public void setUserId(Integer userId) {
            this.userId = userId;
        }

        public String  getImages() {
            return images;
        }

        public void setImages(String images) {
            this.images = images;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getIsDownload() {
            return isDownload;
        }

        public void setIsDownload(String isDownload) {
            this.isDownload = isDownload;
        }

        public String getProfileImage() {
            return profileImage;
        }

        public void setProfileImage(String profileImage) {
            this.profileImage = profileImage;
        }

        public String getFullname() {
            return fullname;
        }

        public void setFullname(String fullname) {
            this.fullname = fullname;
        }

        public Integer getTotalLike() {
            return totalLike;
        }

        public void setTotalLike(Integer totalLike) {
            this.totalLike = totalLike;
        }

        public Integer getTotalShare() {
            return totalShare;
        }

        public void setTotalShare(Integer totalShare) {
            this.totalShare = totalShare;
        }

        public List<String> getPostImage() {
            return postImage;
        }

        public void setPostImage(List<String> postImage) {
            this.postImage = postImage;
        }

        public String getAdded() {
            return added;
        }

        public void setAdded(String added) {
            this.added = added;
        }
    }


}