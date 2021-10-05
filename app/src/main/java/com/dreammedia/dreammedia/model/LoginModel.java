package com.dreammedia.dreammedia.model;

import java.util.List;

public class LoginModel {

    private Boolean status;
    private String message;
    private List<Responce> responce = null;
    private String token;

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

    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }

    public class Responce {

        private String id;
        private String fname;
        private String lname;
        private String fullname;
        private String email;
        private String mobile;
        private String profile_image;
        private String role;
        private String verificationkey;
        private String referral_code;
        private String hash_key;
        private String password;
        private String username;
        private String website;
        private String workdetail;
        private String dob;
        private String place;
        private String cover_pic;
        private String created;
        private String updated;
        private String deleted;
        private String is_active;
        private String is_login;
        private String usertoken;

        public String getId() { return id; }
        public void setId(String id) { this.id = id; }

        public String getFname() { return fname; }
        public void setFname(String fname) { this.fname = fname; }

        public String getLname() { return lname; }
        public void setLname(String lname) { this.lname = lname; }

        public String getFullname() { return fullname; }
        public void setFullname(String fullname) { this.fullname = fullname; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getMobile() { return mobile; }
        public void setMobile(String mobile) { this.mobile = mobile; }

        public String getProfile_image() { return profile_image; }
        public void setProfile_image(String profile_image) { this.profile_image = profile_image; }

        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }

        public String getVerificationkey() { return verificationkey; }
        public void setVerificationkey(String verificationkey) { this.verificationkey = verificationkey; }

        public String getReferral_code() { return referral_code; }
        public void setReferral_code(String referral_code) { this.referral_code = referral_code; }

        public String getHash_key() {  return hash_key; }
        public void setHash_key(String hash_key) { this.hash_key = hash_key; }

        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }

        public String getWebsite() { return website; }
        public void setWebsite(String website) { this.website = website; }

        public String getWorkdetail() { return workdetail; }
        public void setWorkdetail(String workdetail) { this.workdetail = workdetail; }

        public String getDob() { return dob; }
        public void setDob(String dob) { this.dob = dob; }

        public String getPlace() { return place; }
        public void setPlace(String place) { this.place = place; }

        public String getCover_pic() { return cover_pic; }
        public void setCover_pic(String cover_pic) { this.cover_pic = cover_pic; }

        public String getCreated() { return created; }
        public void setCreated(String created) { this.created = created; }

        public String getUpdated() { return updated; }
        public void setUpdated(String updated) { this.updated = updated; }

        public String getDeleted() { return deleted; }
        public void setDeleted(String deleted) { this.deleted = deleted; }

        public String getIs_active() { return is_active; }
        public void setIs_active(String is_active) { this.is_active = is_active; }

        public String getIs_login() { return is_login; }
        public void setIs_login(String is_login) { this.is_login = is_login; }

        public String getUsertoken() { return usertoken; }
        public void setUsertoken(String usertoken) { this.usertoken = usertoken; }

    }

}

