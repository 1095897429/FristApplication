package com.ngbj.browse.bean;

import java.io.Serializable;

/***
 * 上传的数据格式
 */
public class UploadCount implements Serializable{
    private String ad_id;//每个ad都有一个id
    private int show_num;//各条广告的展示次数
    private int click_num;//每条广告的点击次数
    private int click_user_num;//此用户点

    public String getAd_id() {
        return ad_id;
    }

    public void setAd_id(String ad_id) {
        this.ad_id = ad_id;
    }

    public int getShow_num() {
        return show_num;
    }

    public void setShow_num(int show_num) {
        this.show_num = show_num;
    }

    public int getClick_num() {
        return click_num;
    }

    public void setClick_num(int click_num) {
        this.click_num = click_num;
    }

    public int getClick_user_num() {
        return click_user_num;
    }

    public void setClick_user_num(int click_user_num) {
        this.click_user_num = click_user_num;
    }
}
