package com.dreammedia.dreammedia.model;


import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WhoToFollowResponse {

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

    private String totaluser;

    public String getTotaluser() {
        return totaluser;
    }

    public void setTotaluser(String totaluser) {
        this.totaluser = totaluser;
    }

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

    public static class Responce {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("fullname")
        @Expose
        private String fullname;
        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("mobile")
        @Expose
        private String mobile;
        @SerializedName("profile_image")
        @Expose
        private String profileImage;
        @SerializedName("username")
        @Expose
        private Object username;
        @SerializedName("website")
        @Expose
        private Object website;
        @SerializedName("workdetail")
        @Expose
        private Object workdetail;
        @SerializedName("dob")
        @Expose
        private Object dob;
        @SerializedName("place")
        @Expose
        private Object place;
        @SerializedName("cover_pic")
        @Expose
        private Object coverPic;
        @SerializedName("usertoken")
        @Expose

        private String usertoken;
        private String bio;

        private boolean mFllowing;


        public String getBio() {
            return bio;
        }

        public void setBio(String bio) {
            this.bio = bio;
        }

        private int is_follow;

        public int getIs_follow() {
            return is_follow;
        }

        public void setIs_follow(int is_follow) {
            this.is_follow = is_follow;
        }

        public boolean getmFllowing() {
            return mFllowing;
        }

        public void setmFllowing(boolean mFllowing) {
            this.mFllowing = mFllowing;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getFullname() {
            return fullname;
        }

        public void setFullname(String fullname) {
            this.fullname = fullname;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getProfileImage() {
            return profileImage;
        }

        public void setProfileImage(String profileImage) {
            this.profileImage = profileImage;
        }

        public Object getUsername() {
            return username;
        }

        public void setUsername(Object username) {
            this.username = username;
        }

        public Object getWebsite() {
            return website;
        }

        public void setWebsite(Object website) {
            this.website = website;
        }

        public Object getWorkdetail() {
            return workdetail;
        }

        public void setWorkdetail(Object workdetail) {
            this.workdetail = workdetail;
        }

        public Object getDob() {
            return dob;
        }

        public void setDob(Object dob) {
            this.dob = dob;
        }

        public Object getPlace() {
            return place;
        }

        public void setPlace(Object place) {
            this.place = place;
        }

        public Object getCoverPic() {
            return coverPic;
        }

        public void setCoverPic(Object coverPic) {
            this.coverPic = coverPic;
        }

        public String getUsertoken() {
            return usertoken;
        }

        public void setUsertoken(String usertoken) {
            this.usertoken = usertoken;
        }

    }

}

