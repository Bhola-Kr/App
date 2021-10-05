package com.dreammedia.dreammedia.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WalletAmountResponse {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("responce")
    @Expose
    private List<Responce> responce = null;
    @SerializedName("currentplan")
    @Expose
    private List<Currentplan> currentplan = null;

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

    public List<Currentplan> getCurrentplan() {
        return currentplan;
    }

    public void setCurrentplan(List<Currentplan> currentplan) {
        this.currentplan = currentplan;
    }

    public class Currentplan {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("user_id")
        @Expose
        private Integer userId;
        @SerializedName("plan_id")
        @Expose
        private Integer planId;
        @SerializedName("payment_id")
        @Expose
        private String paymentId;
        @SerializedName("use_coin")
        @Expose
        private String useCoin;
        @SerializedName("amount")
        @Expose
        private String amount;
        @SerializedName("payment_status")
        @Expose
        private Integer paymentStatus;
        @SerializedName("created")
        @Expose
        private String created;
        @SerializedName("updated")
        @Expose
        private String updated;
        @SerializedName("is_active")
        @Expose
        private Integer isActive;
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("month")
        @Expose
        private String month;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getUserId() {
            return userId;
        }

        public void setUserId(Integer userId) {
            this.userId = userId;
        }

        public Integer getPlanId() {
            return planId;
        }

        public void setPlanId(Integer planId) {
            this.planId = planId;
        }

        public String getPaymentId() {
            return paymentId;
        }

        public void setPaymentId(String paymentId) {
            this.paymentId = paymentId;
        }

        public String getUseCoin() {
            return useCoin;
        }

        public void setUseCoin(String useCoin) {
            this.useCoin = useCoin;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public Integer getPaymentStatus() {
            return paymentStatus;
        }

        public void setPaymentStatus(Integer paymentStatus) {
            this.paymentStatus = paymentStatus;
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

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getMonth() {
            return month;
        }

        public void setMonth(String month) {
            this.month = month;
        }

    }

    public class Responce {

        @SerializedName("totalwalletcoin")
        @Expose
        private Integer totalwalletcoin;

        public Integer getTotalwalletcoin() {
            return totalwalletcoin;
        }

        public void setTotalwalletcoin(Integer totalwalletcoin) { this.totalwalletcoin = totalwalletcoin;
        }

    }

}