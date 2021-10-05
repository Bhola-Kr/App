package com.dreammedia.dreammedia.model;


import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProfileResponse {

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
        private String username;
        @SerializedName("website")
        @Expose
        private String website;
        @SerializedName("workdetail")
        @Expose
        private String workdetail;
        @SerializedName("dob")
        @Expose
        private String dob;
        @SerializedName("place")
        @Expose
        private String place;
        @SerializedName("cover_pic")
        @Expose
        private String coverPic;
        @SerializedName("password")
        @Expose
        private String password;
        @SerializedName("hash_key")
        @Expose
        private String hashKey;
        @SerializedName("total_post")
        @Expose
        private Integer totalPost;
        @SerializedName("total_following")
        @Expose
        private Integer totalFollowing;
        @SerializedName("total_follower")
        @Expose
        private Integer totalFollower;

        private String bio;

        public String getBio() {
            return bio;
        }

        public void setBio(String bio) {
            this.bio = bio;
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

        public String getUsername() { return username; }

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

        public String getCoverPic() {
            return coverPic;
        }

        public void setCoverPic(String coverPic) {
            this.coverPic = coverPic;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getHashKey() {
            return hashKey;
        }

        public void setHashKey(String hashKey) {
            this.hashKey = hashKey;
        }

        public Integer getTotalPost() {
            return totalPost;
        }

        public void setTotalPost(Integer totalPost) {
            this.totalPost = totalPost;
        }

        public Integer getTotalFollowing() {
            return totalFollowing;
        }

        public void setTotalFollowing(Integer totalFollowing) {
            this.totalFollowing = totalFollowing;
        }

        public Integer getTotalFollower() {
            return totalFollower;
        }

        public void setTotalFollower(Integer totalFollower) {
            this.totalFollower = totalFollower;
        }

    }

}

