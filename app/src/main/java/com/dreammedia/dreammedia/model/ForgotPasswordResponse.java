package com.dreammedia.dreammedia.model;

import java.util.List;

public class ForgotPasswordResponse {

    private Boolean status;
    private String message;
    private List<Responce> responce = null;
    private String otp;
    public Boolean getStatus() { return status; }
    public void setStatus(Boolean status) { this.status = status; }

    public String getMessage() {   return message; }
    public void setMessage(String message) {  this.message = message; }

    public List<Responce> getResponce() {   return responce; }
    public void setResponce(List<Responce> responce) {   this.responce = responce; }

    public String getOtp() {    return otp; }
    public void setOtp(String otp) { this.otp = otp; }

    public class Responce {

        private String id;
        private String fullname;
        private String email;
        private String mobile;
        private String profile_image;
        private Object username;
        private Object website;
        private Object workdetail;
        private Object dob;
        private Object place;
        private Object cover_pic;
        private String password;
        private String hash_key;

        public String getId() { return id; }
        public void setId(String id) {
            this.id = id;
        }

        public String getFullname() {
            return fullname;
        }
        public void setFullname(String fullname) {
            this.fullname = fullname;
        }

        public String getEmail() { return email; }
        public void setEmail(String email) {
            this.email = email;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getProfileImage() { return profile_image; }
        public void setProfileImage(String profileImage) { this.profile_image = profileImage; }

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

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getProfile_image() {
            return profile_image;
        }

        public void setProfile_image(String profile_image) {
            this.profile_image = profile_image;
        }

        public Object getCover_pic() {
            return cover_pic;
        }

        public void setCover_pic(Object cover_pic) {
            this.cover_pic = cover_pic;
        }

        public String getHash_key() {
            return hash_key;
        }

        public void setHash_key(String hash_key) {
            this.hash_key = hash_key;
        }
    }

}

