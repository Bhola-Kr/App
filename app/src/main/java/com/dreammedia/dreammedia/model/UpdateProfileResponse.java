package com.dreammedia.dreammedia.model;


public class UpdateProfileResponse {

        private Boolean status;
        private String message;
        private String responce;

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

        public String getResponce() {
            return responce;
        }
        public void setResponce(String responce) {
            this.responce = responce;
        }

}
