package com.dreammedia.dreammedia.model;


import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FollowrsResponse {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("responce")
    @Expose
    private List<Responce> responce = null;
    @SerializedName("pagecount")
    @Expose
    private String pagecount;

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

    public String getPagecount() {
        return pagecount;
    }

    public void setPagecount(String pagecount) {
        this.pagecount = pagecount;
    }

    public class Responce {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("sender_id")
        @Expose
        private Integer senderId;
        @SerializedName("reciever_id")
        @Expose
        private Integer recieverId;
        @SerializedName("is_approve")
        @Expose
        private Integer isApprove;
        @SerializedName("fullname")
        @Expose
        private String fullname;
        @SerializedName("user_id")
        @Expose
        private Integer userId;
        @SerializedName("profile_image")
        @Expose
        private String profileImage;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getSenderId() {
            return senderId;
        }

        public void setSenderId(Integer senderId) {
            this.senderId = senderId;
        }

        public Integer getRecieverId() {
            return recieverId;
        }

        public void setRecieverId(Integer recieverId) {
            this.recieverId = recieverId;
        }

        public Integer getIsApprove() {
            return isApprove;
        }

        public void setIsApprove(Integer isApprove) {
            this.isApprove = isApprove;
        }

        public String getFullname() {
            return fullname;
        }

        public void setFullname(String fullname) {
            this.fullname = fullname;
        }

        public Integer getUserId() {
            return userId;
        }

        public void setUserId(Integer userId) {
            this.userId = userId;
        }

        public String getProfileImage() {
            return profileImage;
        }

        public void setProfileImage(String profileImage) {
            this.profileImage = profileImage;
        }

    }

}

