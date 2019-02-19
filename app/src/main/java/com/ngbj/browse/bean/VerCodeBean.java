package com.ngbj.browse.bean;

import java.io.Serializable;

public class VerCodeBean implements Serializable {


        /**
         * id : 21
         * mobile : 18201727135
         * code : 9142
         * create_time : 1539753731
         * send_num : 21
         */

        private int id;
        private String mobile;
        private int code;
        private int create_time;
        private int send_num;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public int getCreate_time() {
            return create_time;
        }

        public void setCreate_time(int create_time) {
            this.create_time = create_time;
        }

        public int getSend_num() {
            return send_num;
        }

        public void setSend_num(int send_num) {
            this.send_num = send_num;
        }
}
