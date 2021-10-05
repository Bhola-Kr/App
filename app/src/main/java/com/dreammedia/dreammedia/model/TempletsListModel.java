package com.dreammedia.dreammedia.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;


public class TempletsListModel {

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
    @SerializedName("perpage")
    @Expose
    private Integer perpage;
    @SerializedName("totalcount")
    @Expose
    private Integer totalcount;

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

    public Integer getPerpage() {
        return perpage;
    }

    public void setPerpage(Integer perpage) {
        this.perpage = perpage;
    }

    public Integer getTotalcount() {
        return totalcount;
    }

    public void setTotalcount(Integer totalcount) {
        this.totalcount = totalcount;
    }

    public static class Responce {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("category")
        @Expose
        private Integer category;
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("type")
        @Expose
        private String type;
        @SerializedName("image")
        @Expose
        private String image;
        @SerializedName("created")
        @Expose
        private String created;
        @SerializedName("updated")
        @Expose
        private String updated;
        @SerializedName("is_active")
        @Expose
        private Integer isActive;
        @SerializedName("total_post")
        @Expose
        private Integer totalPost;

        private String tempid;

        public String getTempid() { return tempid; }

        public void setTempid(String tempid) { this.tempid = tempid; }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getCategory() {
            return category;
        }

        public void setCategory(Integer category) {
            this.category = category;
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

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
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

        public Integer getTotalPost() {
            return totalPost;
        }

        public void setTotalPost(Integer totalPost) {
            this.totalPost = totalPost;
        }

    }

}