package com.ngbj.browse.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

import java.io.Serializable;

/***
 * 每个广告的点击量
 * 需要记录的：
 *  点击次数   展示次数  点击的人数只能是0,1（这个用sp存储）
 */
@Entity
public class CountData implements Serializable {
    @Id(autoincrement = true)
    private Long id;
    private String ad_id;//每个ad都有一个id
    private int show_num;//各条广告的展示次数
    private int click_num;//每条广告的点击次数
    private int click_user_num;//此用户点
    private String adShowName;//各条广告的展示名称
    private String show_position;//各条广告的展示位置 -- 大模块
    private String img_url;
    private String type;//类型 0 -- 广告
    private String ad_link;//广告的链接

    public CountData() {
    }

    public String getAd_link() {
        return ad_link;
    }

    public void setAd_link(String ad_link) {
        this.ad_link = ad_link;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getAdShowName() {
        return adShowName;
    }

    public void setAdShowName(String adShowName) {
        this.adShowName = adShowName;
    }

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

    public String getShow_position() {
        return show_position;
    }

    public void setShow_position(String show_position) {
        this.show_position = show_position;
    }

    public CountData(String ad_id, int show_num, String adShowName) {
        this.ad_id = ad_id;
        this.show_num = show_num;
        this.adShowName = adShowName;
    }

    public CountData(String ad_id, int show_num, int click_num, int click_user_num, String adShowName) {
        this.ad_id = ad_id;
        this.show_num = show_num;
        this.click_num = click_num;
        this.click_user_num = click_user_num;
        this.adShowName = adShowName;
    }

    public CountData(String ad_id, int show_num, int click_num, String adShowName) {
        this.ad_id = ad_id;
        this.show_num = show_num;
        this.click_num = click_num;
        this.adShowName = adShowName;
    }

    public CountData(String ad_id, int show_num, int click_num, int click_user_num, String adShowName, String show_position, String img_url) {
        this.ad_id = ad_id;
        this.show_num = show_num;
        this.click_num = click_num;
        this.click_user_num = click_user_num;
        this.adShowName = adShowName;
        this.show_position = show_position;
        this.img_url = img_url;
    }

    public CountData(String ad_id, int show_num, int click_num, int click_user_num, String adShowName, String show_position, String img_url, String type, String ad_link) {
        this.ad_id = ad_id;
        this.show_num = show_num;
        this.click_num = click_num;
        this.click_user_num = click_user_num;
        this.adShowName = adShowName;
        this.show_position = show_position;
        this.img_url = img_url;
        this.type = type;
        this.ad_link = ad_link;
    }

    @Generated(hash = 367822676)
    public CountData(Long id, String ad_id, int show_num, int click_num, int click_user_num, String adShowName, String show_position, String img_url, String type,
            String ad_link) {
        this.id = id;
        this.ad_id = ad_id;
        this.show_num = show_num;
        this.click_num = click_num;
        this.click_user_num = click_user_num;
        this.adShowName = adShowName;
        this.show_position = show_position;
        this.img_url = img_url;
        this.type = type;
        this.ad_link = ad_link;
    }





    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
