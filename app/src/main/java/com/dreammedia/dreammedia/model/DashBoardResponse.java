package com.dreammedia.dreammedia.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class DashBoardResponse {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("responce")
    @Expose
    private Responce responce;
    @SerializedName("pagecount")
    @Expose
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
        private List<Randomuser> randomuser = null;
        private List<Randoad> randoads = null;

        private String type ;
        private String video_url ;
        private String created;
        private String added;
        private String is_video;
        boolean isLiked ;
        int is_like ;
        private Boolean followVisiblity ;


        public String getIs_video() {
            return is_video;
        }

        public void setIs_video(String is_video) {
            this.is_video = is_video;
        }

        public boolean isLiked() {
            return isLiked;
        }

        public void setLiked(boolean liked) {
            isLiked = liked;
        }

        public String getVideo_url() { return video_url; }
        public void setVideo_url(String video_url) { this.video_url = video_url; }

        public String getType() { return type; }
        public void setType(String type) { this.type = type; }

        public String getAdded() {
            return added;
        }
        public void setAdded(String added) {
            this.added = added;
        }

        public Boolean getFollowVisiblity() { return followVisiblity; }
        public void setFollowVisiblity(Boolean followVisiblity) { this.followVisiblity = followVisiblity; }

        public String getCreated() { return created; }
        public void setCreated(String created) { this.created = created; }

        public int getIs_like() { return is_like; }
        public void setIs_like(int is_like) { this.is_like = is_like; }

        public boolean getidLiked() { return isLiked; }
        public void setisLiked(boolean liked) { isLiked = liked; }

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

        public void setImages(String images) { this.images = images; }

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

        public Integer getTotalLike() { return totalLike; }

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

        public List<Randomuser> getRandomuser() {
            return randomuser;
        }

        public void setRandomuser(List<Randomuser> randomuser) {
            this.randomuser = randomuser;
        }

        public List<Randoad> getRandoads() {
            return randoads;
        }

        public void setRandoads(List<Randoad> randoads) {
            this.randoads = randoads;
        }

    }

    public class Randoad {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("type")
        @Expose
        private String type;
        @SerializedName("link")
        @Expose
        private String link;
        @SerializedName("banner")
        @Expose
        private String banner;
        @SerializedName("video")
        @Expose
        private String video;
        @SerializedName("start_time")
        @Expose
        private String startTime;
        @SerializedName("end_time")
        @Expose
        private String endTime;
        @SerializedName("coverimage")
        @Expose
        private String coverimage;
        @SerializedName("created")
        @Expose
        private String created;
        @SerializedName("updated")
        @Expose
        private String updated;
        @SerializedName("is_active")
        @Expose
        private Integer isActive;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public String getBanner() {
            return banner;
        }

        public void setBanner(String banner) {
            this.banner = banner;
        }

        public String getVideo() {
            return video;
        }

        public void setVideo(String video) {
            this.video = video;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getCoverimage() {
            return coverimage;
        }

        public void setCoverimage(String coverimage) {
            this.coverimage = coverimage;
        }

        public String getCreated() {
            return created;
        }

        public void setCreated(String created) {
            this.created = created;
        }

        public String getUpdated() {
            return updated;
        }

        public void setUpdated(String updated) {
            this.updated = updated;
        }

        public Integer getIsActive() {
            return isActive;
        }

        public void setIsActive(Integer isActive) {
            this.isActive = isActive;
        }

    }

    public class Randomuser {

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


        private  boolean mFllowing ;

        public boolean getmFllowing() { return mFllowing; }
        public void setmFllowing(boolean mFllowing) { this.mFllowing = mFllowing; }

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

    }

    public class Responce {

        @SerializedName("slider")
        @Expose
        private List<List<String>> slider = null;
        @SerializedName("posts")
        @Expose
        private List<Post> posts = null;

        private  String totalposts ;

        public List<List<String>> getSlider() {
            return slider;
        }

        public void setSlider(List<List<String>> slider) {
            this.slider = slider;
        }

        public List<Post> getPosts() {
            return posts;
        }

        public void setPosts(List<Post> posts) {
            this.posts = posts;
        }

        public String getTotalposts() {
            return totalposts;
        }

        public void setTotalposts(String totalposts) {
            this.totalposts = totalposts;
        }
    }

}

