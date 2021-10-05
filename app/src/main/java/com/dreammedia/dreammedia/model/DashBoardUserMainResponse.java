package com.dreammedia.dreammedia.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;


public class DashBoardUserMainResponse {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("responce")
    @Expose
    private Responce responce;

    private Integer pagecount;
    private Integer perpage;

    public Integer getPerpage() {
        return perpage;
    }

    public void setPerpage(Integer perpage) {
        this.perpage = perpage;
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

    public Responce getResponce() {
        return responce;
    }

    public void setResponce(Responce responce) {
        this.responce = responce;
    }

    public Integer getPagecount() {
        return pagecount;
    }

    public void setPagecount(Integer pagecount) {
        this.pagecount = pagecount;
    }

    public class Alluser {

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

        private  boolean mFllowing ;

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

    public static class Post {

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

        private Integer is_like ;
        private String created;
        private String added;
        private String type ;
        private String video_url ;
        private String is_video ;

        public String getIs_video() {
            return is_video;
        }

        public void setIs_video(String is_video) {
            this.is_video = is_video;
        }

        public String getVideo_url() {
            return video_url;
        }

        public void setVideo_url(String video_url) {
            this.video_url = video_url;
        }

        public int getIs_like() { return is_like; }
        public void setIs_like(int is_like) { this.is_like = is_like; }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getAdded() {
            return added;
        }

        public void setAdded(String added) {
            this.added = added;
        }

        public String getCreated() { return created; }
        public void setCreated(String created) { this.created = created; }

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

        public String getImages() {
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

    }

    public class Responce {

        @SerializedName("user")
        @Expose
        private List<User> user = null;
        @SerializedName("allusers")
        @Expose
        private List<Alluser> allusers = null;
        @SerializedName("posts")
        @Expose
        private List<Post> posts = null;

        private  Integer totalposts ;

        public Integer getTotalposts() {
            return totalposts;
        }

        public void setTotalposts(Integer totalposts) {
            this.totalposts = totalposts;
        }

        public List<User> getUser() {
            return user;
        }

        public void setUser(List<User> user) {
            this.user = user;
        }

        public List<Alluser> getAllusers() {
            return allusers;
        }

        public void setAllusers(List<Alluser> allusers) {
            this.allusers = allusers;
        }

        public List<Post> getPosts() {
            return posts;
        }

        public void setPosts(List<Post> posts) {
            this.posts = posts;
        }

    }

    public class User {

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

        private Integer is_follow;

        private String bio;

        public String getBio() {
            return bio;
        }

        public void setBio(String bio) {
            this.bio = bio;
        }

        public Integer getIs_follow() {
            return is_follow;
        }

        public void setIs_follow(Integer is_follow) {
            this.is_follow = is_follow;
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

