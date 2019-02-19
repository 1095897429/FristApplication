package com.ngbj.browse.bean;

import java.io.Serializable;

public class LoginBean implements Serializable{

    /**
     * success : true
     * code : 200
     * data : {"access_token":"WHok6FEHhY1_XmNUzOTfXWYOfbsV5jS0","mobile":"18616541823","nickname":"llq40084877","gender":null}
     * message : OK
     */

    private boolean success;
    private int code;
    private DataBean data;
    private String message;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static class DataBean {
        /**
         * access_token : WHok6FEHhY1_XmNUzOTfXWYOfbsV5jS0
         * mobile : 18616541823
         * nickname : llq40084877
         * gender : null
         */
        private int expire_time;
        private String mobile;
        private String nickname;
        private String gender;
        private String head_img;
        private String access_token;

        public String getHead_img() {
            return head_img;
        }

        public void setHead_img(String head_img) {
            this.head_img = head_img;
        }

        public String getAccess_token() {
            return access_token;
        }

        public void setAccess_token(String access_token) {
            this.access_token = access_token;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public int getExpire_time() {
            return expire_time;
        }

        public void setExpire_time(int expire_time) {
            this.expire_time = expire_time;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }
    }
}
