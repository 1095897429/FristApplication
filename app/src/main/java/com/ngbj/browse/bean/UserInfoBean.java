package com.ngbj.browse.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

import java.io.Serializable;

@Entity
public class UserInfoBean implements Serializable {

        /**
         * access_token : IXbi33_K3wrTC0lkv9KKMF6oTTyK1Sal
         * expire_time : 1542537712
         * mobile : 18616541823
         * nickname : llq40084877d
         * gender : 0
         * head_img : http://pgfiz5ht4.bkt.clouddn.com/xnbrowser/user/20181019/SmndWssM-c-KTI7N.png
         */
        @Id(autoincrement = true)
        private Long id;
        private String access_token;
        private int expire_time;
        private String mobile;
        private String nickname;
        private String gender;
        private String head_img;


    public UserInfoBean() {
    }

    @Generated(hash = 1352292971)
    public UserInfoBean(Long id, String access_token, int expire_time, String mobile, String nickname,
            String gender, String head_img) {
        this.id = id;
        this.access_token = access_token;
        this.expire_time = expire_time;
        this.mobile = mobile;
        this.nickname = nickname;
        this.gender = gender;
        this.head_img = head_img;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccess_token() {
            return access_token;
        }

        public void setAccess_token(String access_token) {
            this.access_token = access_token;
        }

        public int getExpire_time() {
            return expire_time;
        }

        public void setExpire_time(int expire_time) {
            this.expire_time = expire_time;
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


        public String getHead_img() {
            return head_img;
        }

        public void setHead_img(String head_img) {
            this.head_img = head_img;
        }
}
