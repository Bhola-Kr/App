package com.dreammedia.dreammedia.model;

import java.util.List;

public class OtpVerifyModel {

    private Boolean status;
    private String message;
    private List<Responce> responce = null;
    private String token;

    public Boolean getStatus() { return status; }
    public void setStatus(Boolean status) { this.status = status; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public List<Responce> getResponce() { return responce; }
    public void setResponce(List<Responce> responce) { this.responce = responce; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public class Responce {

        private String id;
        private String fullname;
        private String email;
        private String mobile;
        private String profile_image;
        private String username;
        private String website;
        private String workdetail;
        private String dob;
        private String place;
        private String cover_pic;
        private String referral_code;

        public String getId() {
            return id;
        }

        public String getReferral_code() {
            return referral_code;
        }

        public void setReferral_code(String referral_code) {
            this.referral_code = referral_code;
        }

        public void setId(String id) {
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

        public String getProfile_image() {
            return profile_image;
        }

        public void setProfile_image(String profile_image) {
            this.profile_image = profile_image;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getWebsite() {
            return website;
        }

        public void setWebsite(String website) {
            this.website = website;
        }

        public String getWorkdetail() {
            return workdetail;
        }

        public void setWorkdetail(String workdetail) {
            this.workdetail = workdetail;
        }

        public String getDob() {
            return dob;
        }

        public void setDob(String dob) {
            this.dob = dob;
        }

        public String getPlace() {
            return place;
        }

        public void setPlace(String place) {
            this.place = place;
        }

        public String getCover_pic() {
            return cover_pic;
        }

        public void setCover_pic(String cover_pic) {
            this.cover_pic = cover_pic;
        }
    }

}

